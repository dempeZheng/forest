package com.dempe.forest.transport;

import com.dempe.forest.client.Promise;
import com.dempe.forest.codec.ForestDecoder;
import com.dempe.forest.codec.ForestEncoder;
import com.dempe.forest.codec.Message;
import com.dempe.forest.codec.Response;
import com.dempe.forest.core.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
public class NettyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    protected Bootstrap b;
    protected EventLoopGroup group;
    protected Channel channel;
    private String host;
    private int port;
    private ClientHandler handler;

    public NettyClient(String host, int port) throws InterruptedException {
        this.host = host;
        this.port = port;
        handler = new ClientHandler();
        init();
    }

    private void init() throws InterruptedException {
        b = new Bootstrap();
        group = new NioEventLoopGroup(4);
        b.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        initClientChannel(ch);
                    }
                });

    }

    public void initClientChannel(SocketChannel ch) {
        ch.pipeline().addLast("encode", new ForestEncoder());
        ch.pipeline().addLast("decode", new ForestDecoder());
        ch.pipeline().addLast("handler", handler);

    }

    public ChannelFuture connect() throws InterruptedException {
        ChannelFuture connect = b.connect(host, port).sync();
        channel = connect.channel();
        return connect;

    }


    public NettyResponseFuture<Response> write(Message message, long timeOut) {
        channel.writeAndFlush(message);
        NettyResponseFuture responseFuture = new NettyResponseFuture(System.currentTimeMillis(), timeOut, message, channel, new Promise<Response>());
        handler.registerCallbackMap(message.getHeader().getMessageID(), responseFuture);
        return responseFuture;
    }

    public void callback(Message message, long timeOut, Promise<Response> promise) {
        channel.writeAndFlush(message);
        NettyResponseFuture responseFuture = new NettyResponseFuture(System.currentTimeMillis(), timeOut, message, channel, promise);
        handler.registerCallbackMap(message.getHeader().getMessageID(), responseFuture);
    }


}
