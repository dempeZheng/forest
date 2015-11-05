package com.dempe.ketty.srv.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/3
 * Time: 15:01
 * To change this template use File | Settings | File Templates.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private HttpServerContext context;

    public HttpServerInitializer(HttpServerContext context) {
        this.context = context;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 设置读超时
        pipeline.addLast("timeout", new ReadTimeoutHandler(context.builder.getReadTimeout()))
                .addLast("decoder", new HttpRequestDecoder())
                .addLast("encoder", new HttpResponseEncoder())
                .addLast("handler", new HttpDispatcherHandler(context));
    }
}
