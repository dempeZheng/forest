package com.dempe.forest.core.handler;

import com.dempe.forest.codec.Message;
import com.dempe.forest.core.URIMapping;
import com.dempe.forest.core.invoker.InvokerWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Executor;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 17:04
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorHandler extends SimpleChannelInboundHandler<Message> {

    private URIMapping mapping;

    private Executor executor;

    public ProcessorHandler(URIMapping mapping, Executor executor) {
        this.mapping = mapping;
        this.executor = executor;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

        short uri = message.getHeader().getUri();
        final InvokerWrapper invokerWrapperByURI = mapping.getInvokerWrapperByURI(uri);
        executor.execute(new InvokerRunnable(invokerWrapperByURI, channelHandlerContext));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

class InvokerRunnable implements Runnable {

    private InvokerWrapper invokerWrapper;
    private ChannelHandlerContext ctx;

    public InvokerRunnable(InvokerWrapper invokerWrapper, ChannelHandlerContext ctx) {
        this.invokerWrapper = invokerWrapper;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        // todo exception handle
        Object result = invokerWrapper.invoke();
        //
        Message message = invokerWrapper.getMessage();
//        message.setPayload(message);
        ctx.writeAndFlush(result);

    }

}
