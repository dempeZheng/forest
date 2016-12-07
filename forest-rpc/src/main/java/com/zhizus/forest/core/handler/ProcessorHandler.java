package com.zhizus.forest.core.handler;

import com.zhizus.forest.AnnotationRouterMapping;
import com.zhizus.forest.ForestContext;
import com.zhizus.forest.ForestExecutorGroup;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Response;
import com.zhizus.forest.common.codec.compress.Compress;
import com.zhizus.forest.common.codec.serialize.Serialization;
import com.zhizus.forest.core.ActionMethod;
import com.zhizus.forest.common.CompressType;
import com.zhizus.forest.common.SerializeType;
import com.zhizus.forest.common.exception.ForestErrorMsgConstant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
/**
 * Created by Dempe on 2016/12/7.
 */
public class ProcessorHandler extends SimpleChannelInboundHandler<Message> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessorHandler.class);
    // 业务线程池
    private static ForestExecutorGroup executorGroup;
    private AnnotationRouterMapping mapping;

    public ProcessorHandler(AnnotationRouterMapping mapping, ForestExecutorGroup executorGroup) {
        this.mapping = mapping;
        this.executorGroup = executorGroup;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        String uri = message.getHeader().getUri();
        final ActionMethod actionMethod = mapping.getInvokerWrapperByURI(uri);
        if (actionMethod == null) {
            LOGGER.warn("no mapping methodName:{}", uri);
            return;
        }
        executorGroup.execute(actionMethod.getGroup(), new InvokerRunnable(actionMethod, message, channelHandlerContext));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

class InvokerRunnable implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(InvokerRunnable.class);

    private ChannelHandlerContext ctx;
    private ActionMethod actionMethod;
    private Message message;

    public InvokerRunnable(ActionMethod actionMethod, Message message, ChannelHandlerContext ctx) {
        this.actionMethod = actionMethod;
        this.message = message;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        Response response = new Response();
        Object result = null;
        Byte extend = message.getHeader().getExtend();
        Serialization serialization = SerializeType.getSerializationByExtend(extend);
        Compress compress = CompressType.getCompressTypeByValueByExtend(extend);

        ForestContext.setForestContext(ctx.channel(), message.getHeader());
        byte[] payload = message.getPayload();
        Object[] args = null;
        // req
        try {
            payload = compress.unCompress(payload);
            args = serialization.deserialize(payload, Object[].class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setForestErrorMsg(ForestErrorMsgConstant.FRAMEWORK_DECODE_ERROR);
        }
        try {
            result = actionMethod.rateLimiterInvoker(args);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setForestErrorMsg(ForestErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
        } finally {
            ForestContext.removeForestContext();
        }
        // rsp
        try {
            response.setResult(result);
            byte[] rspPayload = serialization.serialize(response);
            message.setPayload(compress.compress(rspPayload));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            response.setForestErrorMsg(ForestErrorMsgConstant.FRAMEWORK_ENCODE_ERROR);

        }
        ctx.writeAndFlush(message);
    }

}
