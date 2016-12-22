package com.zhizus.forest.transport;

import com.zhizus.forest.IRouter;
import com.zhizus.forest.common.config.ServerConfig;
import com.zhizus.forest.handler.HttpProcessorHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by Dempe on 2016/12/22.
 */
public class HttpServer extends AbstractServer {

    public HttpServer(IRouter iRouter, ServerConfig config, int port) throws InterruptedException {
        super(iRouter, config, port);
    }

    @Override
    public ChannelInitializer<SocketChannel> newChannelInitializer(final IRouter iRouter) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                // 设置读超时
                pipeline.addLast("decoder", new HttpRequestDecoder())
                        .addLast("encoder", new HttpResponseEncoder())
                        .addLast("handler", new HttpProcessorHandler(iRouter));
            }
        };
    }
}
