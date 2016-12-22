package com.zhizus.forest.transport;

import com.zhizus.forest.IRouter;
import com.zhizus.forest.codec.ForestDecoder;
import com.zhizus.forest.codec.ForestEncoder;
import com.zhizus.forest.common.config.ServerConfig;
import com.zhizus.forest.handler.ForestProcessorHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by Dempe on 2016/12/9.
 */
public class ForestServer extends AbstractServer {

    public ForestServer(IRouter iRouter, ServerConfig config, int port) throws InterruptedException {
        super(iRouter, config, port);
    }

    public ChannelInitializer<SocketChannel> newChannelInitializer(final IRouter iRouter) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("decoder", new ForestDecoder());
                ch.pipeline().addLast("encoder", new ForestEncoder());
                ch.pipeline().addLast("processor", new ForestProcessorHandler(iRouter));
            }
        };

    }
}
