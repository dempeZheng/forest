package com.dempe.forest.transport;

import com.dempe.forest.ClientConfig;
import com.dempe.forest.client.Connection;
import com.dempe.forest.codec.ForestDecoder;
import com.dempe.forest.codec.ForestEncoder;
import com.dempe.forest.codec.Response;
import com.dempe.forest.core.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

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
    private String host;
    private int port;

    public NettyClient(ClientConfig config) throws InterruptedException {
        this.host = config.host();
        this.port = config.port();
        init();
//        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(
//                new TimeoutMonitor("timeout_monitor_" + host + "_" + port), 100, 100, TimeUnit.MILLISECONDS);
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
        ch.pipeline().addLast("handler", new ClientHandler());

    }

    public ChannelFuture connect() {
        ChannelFuture connect = b.connect(host, port);
        connect.awaitUninterruptibly();
        return connect;
    }

    public boolean close() {
        return false;
    }

    public boolean isConnected() {
        return true;
    }

    class TimeoutMonitor implements Runnable {
        private String name;

        public TimeoutMonitor(String name) {
            this.name = name;
        }

        public void run() {
            long currentTime = System.currentTimeMillis();
            for (Map.Entry<Long, NettyResponseFuture<Response>> entry : Connection.callbackMap.entrySet()) {
                try {
                    NettyResponseFuture future = entry.getValue();
                    if (future.getCreateTime() + future.getTimeOut() < currentTime) {
                        // timeout: remove from callback list, and then cancel
                        Connection.callbackMap.remove(entry.getKey());
                    }
                } catch (Exception e) {
                    LOGGER.error(name + " clear timeout future Error: methodName="
                            + entry.getValue().getRequest().getHeader().getUri() + " requestId=" + entry.getKey(), e);
                }
            }
        }
    }

}
