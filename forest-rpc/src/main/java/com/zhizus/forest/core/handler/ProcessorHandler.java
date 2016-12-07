package com.zhizus.forest.core.handler;

import com.zhizus.forest.AnnotationRouterMapping;
import com.zhizus.forest.ForestContext;
import com.zhizus.forest.ForestExecutorGroup;
import com.zhizus.forest.common.MessageType;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Request;
import com.zhizus.forest.common.codec.Response;
import com.zhizus.forest.common.exception.ForestErrorMsgConstant;
import com.zhizus.forest.common.util.ForestUtil;
import com.zhizus.forest.core.ActionMethod;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ProcessorHandler extends SimpleChannelInboundHandler<Message<Request>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessorHandler.class);
    // 业务线程池
    private static ForestExecutorGroup executorGroup;
    private AnnotationRouterMapping mapping;

    public ProcessorHandler(AnnotationRouterMapping mapping, ForestExecutorGroup executorGroup) {
        this.mapping = mapping;
        this.executorGroup = executorGroup;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, Message<Request> message) throws Exception {

        Request request = message.getContent();
        String uri = ForestUtil.buildUri(request.getServiceName(), request.getMethodName());
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
    private Message<Request> message;

    public InvokerRunnable(ActionMethod actionMethod, Message<Request> message, ChannelHandlerContext ctx) {
        this.actionMethod = actionMethod;
        this.message = message;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        Response response = new Response();
        Object result = null;
        ForestContext.setForestContext(ctx.channel(), message);
        try {
            result = actionMethod.rateLimiterInvoker(message.getContent().getArgs());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setForestErrorMsg(ForestErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
        } finally {
            ForestContext.removeForestContext();
        }
        // rsp
        byte extend = (byte) (message.getHeader().getExtend() | MessageType.RESPONSE_MESSAGE_TYPE);
        message.getHeader().setExtend(extend);
        response.setResult(result);
        ctx.writeAndFlush(new Message(message.getHeader(), response));
    }

}
