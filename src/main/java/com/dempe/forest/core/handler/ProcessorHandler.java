package com.dempe.forest.core.handler;

import com.dempe.forest.AnnotationRouterMapping;
import com.dempe.forest.ForestContext;
import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.Response;
import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.ActionMethod;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.exception.ForestErrorMsgConstant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorHandler extends SimpleChannelInboundHandler<Message> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessorHandler.class);

    private AnnotationRouterMapping mapping;

    // 业务线程池
    private static Executor executor;

    public ProcessorHandler(AnnotationRouterMapping mapping, Executor executor) {
        this.mapping = mapping;
        this.executor = executor;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        String uri = message.getHeader().getUri();
        final ActionMethod actionMethod = mapping.getInvokerWrapperByURI(uri);
        if (actionMethod == null) {
            LOGGER.warn("no mapping uri:{}", uri);
            return;
        }
        executor.execute(new InvokerRunnable(actionMethod, message, channelHandlerContext));
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
