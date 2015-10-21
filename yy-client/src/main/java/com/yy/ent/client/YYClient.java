package com.yy.ent.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/20
 * Time: 13:17
 * To change this template use File | Settings | File Templates.
 */
public class YYClient {


    private static final Logger LOGGER = LoggerFactory.getLogger(YYClient.class);

    private Bootstrap b;

    private ChannelFuture f;

    private Channel channel;

    private EventLoopGroup group;

    private String host;
    private int port;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public YYClient(String host, int port) {
        this.host = host;
        this.port = port;
        b = new Bootstrap();
        group = new NioEventLoopGroup();
        b.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new StringEncoder(CharsetUtil.UTF_8))
                                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                                .addLast(new ClientHandler());
                    }
                });

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
        // Make the connection attempt.

    }

    public void connect(final String host, final int port) {
        this.host = host;
        this.port = port;

        try {
            f = b.connect(host, port).sync();
            channel = f.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    channel.closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            LOGGER.info("======connent now ===");
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                LOGGER.error(e.getMessage(), e);
                            }
                            connect(host, port);
                        }
                    });
                }
            }
        }).start();


    }


    public void send(String msg) {
        channel.writeAndFlush(msg);
    }


    public void close() throws IOException {

        try {
            f.channel().closeFuture().sync();
            group.shutdownGracefully();

        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}
