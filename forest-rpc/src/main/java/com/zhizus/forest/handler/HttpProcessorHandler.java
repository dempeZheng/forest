package com.zhizus.forest.handler;

import com.zhizus.forest.ActionMethod;
import com.zhizus.forest.IRouter;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dempe on 2016/12/22.
 */
public class HttpProcessorHandler extends ChannelInboundHandlerAdapter {

    private final IRouter iRouter;

    public HttpProcessorHandler(IRouter iRouter) {
        this.iRouter = iRouter;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {

            HttpRequest request = (HttpRequest) msg;
            HttpMethod method = request.method();
            String uri = request.uri();
            // TODO: 2016/12/22
            ActionMethod router = iRouter.router(uri);


        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

class HttpInvokerRunnable implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(InvokerRunnable.class);

    private ChannelHandlerContext ctx;
    private ActionMethod actionMethod;
    private Message<Request> message;

    public HttpInvokerRunnable(ActionMethod actionMethod, Message<Request> message, ChannelHandlerContext ctx) {
        this.actionMethod = actionMethod;
        this.message = message;
        this.ctx = ctx;
    }

    @Override
    public void run() {

    }

}
