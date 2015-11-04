package com.dempe.ketty.srv.ketty;

import com.dempe.ketty.codec.KettyRespEncoder;
import com.dempe.ketty.codec.KettyRequestDecoder;
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

    private KettyServerContext context;

    public KettyServerInitializer(KettyServerContext context) {
        this.context = context;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("encoder", new KettyRespEncoder())
                .addLast("decoder", new KettyRequestDecoder())
                .addLast("dispatcher", new KettyDispatcherHandler(context));
    }
}
