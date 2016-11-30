package com.dempe.forest.core.handler;

import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.Response;
import com.dempe.forest.codec.compress.Compress;
import com.dempe.forest.codec.serialize.Serialization;
import com.dempe.forest.core.AnnotationRouterMapping;
import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.ForestContext;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.invoker.ActionMethod;
import com.dempe.forest.core.invoker.InvokerWrapper;
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

    private static Executor executor;

    public ProcessorHandler(AnnotationRouterMapping mapping, Executor executor) {
        this.mapping = mapping;
        this.executor = executor;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        String uri = message.getHeader().getUri();
        final ActionMethod actionMethod = mapping.getInvokerWrapperByURI(uri);
        executor.execute(new InvokerRunnable(new InvokerWrapper(actionMethod, message), channelHandlerContext));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

class InvokerRunnable implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(InvokerRunnable.class);
    private InvokerWrapper invokerWrapper;
    private ChannelHandlerContext ctx;

    public InvokerRunnable(InvokerWrapper invokerWrapper, ChannelHandlerContext ctx) {
        this.invokerWrapper = invokerWrapper;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        // todo exception handle
        Message message = invokerWrapper.getMessage();
        Response response = new Response();
        ForestContext.setForestContext(ctx.channel(), message.getHeader());
        Object result = null;
        try {
            result = invokerWrapper.invoke();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            response.setErrMsg(e.getMessage());
            response.setResCode((short) -1);
        } finally {
            ForestContext.removeForestContext();
        }

        Byte extend = message.getHeader().getExtend();
        Serialization serialization = SerializeType.getSerializationByExtend(extend);
        try {
            response.setResult(result);
            byte[] payload = serialization.serialize(response);
            Compress compress = CompressType.getCompressTypeByValueByExtend(extend);
            message.setPayload(compress.compress(payload));

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);

        }
        ctx.writeAndFlush(message);


    }

}
