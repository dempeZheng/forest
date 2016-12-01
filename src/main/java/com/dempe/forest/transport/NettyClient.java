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

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
    private ScheduledFuture<?> timeMonitorFuture = null;
    // 回收过期任务
    private static ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(4);

    public NettyClient(String host, int port) throws InterruptedException {
        this.host = host;
        this.port = port;
        handler = new ClientHandler();
        init();
        timeMonitorFuture = scheduledExecutor.scheduleWithFixedDelay(
                new TimeoutMonitor("timeout_monitor_" + host + "_" + port), 100, 100, TimeUnit.MILLISECONDS);
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

    class TimeoutMonitor implements Runnable {
        private String name;

        public TimeoutMonitor(String name) {
            this.name = name;
        }

        public void run() {
            long currentTime = System.currentTimeMillis();
            for (Map.Entry<Long, NettyResponseFuture<Response>> entry : handler.callbackMap.entrySet()) {
                try {
                    NettyResponseFuture future = entry.getValue();
                    if (future.getCreateTime() + future.getTimeOut() < currentTime) {
                        // timeout: remove from callback list, and then cancel
                        handler.removeCallbackMap(entry.getKey());
                    }
                } catch (Exception e) {
                    LOGGER.error(name + " clear timeout future Error: uri="
                            + entry.getValue().getRequest().getHeader().getUri() + " requestId=" + entry.getKey(), e);
                }
            }
        }
    }


}
