package com.yy.ent.client;

import com.yy.ent.codec.JettyReqEncoder;
import com.yy.ent.codec.JettyRespDecoder;
import com.yy.ent.protocol.JettyReq;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/20
 * Time: 13:17
 * To change this template use File | Settings | File Templates.
 */
public class YYClient {


    private static final Logger LOGGER = LoggerFactory.getLogger(YYClient.class);

    protected Bootstrap b;

    protected ChannelFuture f;

    protected Channel channel;

    protected EventLoopGroup group;


    //private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private String host;

    private int port;

    public YYClient(String host, int port) {
        this.host = host;
        this.port = port;

        b = new Bootstrap();
        group = new NioEventLoopGroup();
        b.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new JettyReqEncoder())
                                .addLast(new JettyRespDecoder())
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

        connect(host, port);
    }


    public void connect(final String host, final int port) {


        try {
            f = b.connect(host, port).sync();
            channel = f.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

//    private void startConnectThread(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    channel.closeFuture().sync();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    executorService.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            LOGGER.info("======connect now ===");
//                            try {
//                                TimeUnit.SECONDS.sleep(1);
//                            } catch (InterruptedException e) {
//                                LOGGER.error(e.getMessage(), e);
//                            }
//                            connect(host, port);
//                        }
//                    });
//                }
//            }
//        }).start();
//    }

    public void close() throws IOException {

        try {
            f.channel().closeFuture().sync();
            group.shutdownGracefully();

        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    public void send(JettyReq request) {
        channel.writeAndFlush(request);

    }
}
