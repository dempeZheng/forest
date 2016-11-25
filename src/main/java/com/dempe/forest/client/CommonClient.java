package com.dempe.forest.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO【严重】 消息发送超时情况，contextMap内上下文对象无法清除，存在内存溢出的风险 待添加定时任务，定期清除超时的contextMap，
 *
 * User: Dempe
 * Date: 2015/12/11
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class CommonClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonClient.class);

    protected Bootstrap b;

    protected EventLoopGroup group;
    protected ChannelPool channelPool;

    private DefaultEventExecutorGroup executorGroup;
    private String host;
    private int port;
    private int nextMessageId = 1;

    public CommonClient(String host, int port) throws InterruptedException {
        this.host = host;
        this.port = port;
        init();
    }

    private int getNextMessageId() {
        int rc = nextMessageId;
        nextMessageId++;
        if (nextMessageId == 0) {
            nextMessageId = 1;
        }
        return rc;
    }

    private void init() throws InterruptedException {
        b = new Bootstrap();
        group = new NioEventLoopGroup(4);
        executorGroup = new DefaultEventExecutorGroup(4,
                new DefaultThreadFactory("worker group"));
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

        channelPool = new ChannelPool(this);
    }

    public void initClientChannel(SocketChannel ch) {

    }

    public ChannelFuture connect() {
        return b.connect(host, port);

    }

    public ChannelFuture connect(final String host, final int port) {
        ChannelFuture f = null;
        try {
            f = b.connect(host, port).sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return f;
    }



}
