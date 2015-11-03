package com.yy.ent.srv.core;

import com.yy.ent.codec.KettyRequestDecoder;
import com.yy.ent.codec.KettyRespEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/3
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
public class KettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private ServerContext context;

    public KettyServerInitializer(ServerContext context) {
        this.context = context;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("encoder", new KettyRespEncoder())
                .addLast("decoder", new KettyRequestDecoder())
                .addLast("dispatcher", new DispatcherHandler(context));
    }
}
