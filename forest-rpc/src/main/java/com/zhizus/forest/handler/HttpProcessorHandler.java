package com.zhizus.forest.handler;

import com.zhizus.forest.IRouter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

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


        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
