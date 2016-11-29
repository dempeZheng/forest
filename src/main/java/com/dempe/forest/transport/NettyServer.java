package com.dempe.forest.transport;

import com.dempe.forest.codec.ForestDecoder;
import com.dempe.forest.codec.ForestEncoder;
import com.dempe.forest.core.AnnotationRouterMapping;
import com.dempe.forest.core.StandardThreadExecutor;
import com.dempe.forest.core.handler.ProcessorHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
public class NettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ServerBootstrap bootstrap;
    private Channel channel;
    private Executor executor;
    private AnnotationRouterMapping uriMapping;


    public NettyServer(Executor executor, AnnotationRouterMapping mapping) throws InterruptedException {
        this.executor = executor;
        this.uriMapping = mapping;
        doBind();
    }

    public NettyServer(AnnotationRouterMapping mapping) throws InterruptedException {
        this.uriMapping = mapping;
        doBind();
    }


    protected void doBind() throws InterruptedException {
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        if (executor == null) {
            executor = new StandardThreadExecutor();
        }
        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
//                .option(ChannelOption.SO_BACKLOG, getServerConf().getAccepts())
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("decoder", new ForestDecoder());
                ch.pipeline().addLast("encoder", new ForestEncoder());
                ch.pipeline().addLast("processor", new ProcessorHandler(uriMapping, executor));
            }
        });

        ChannelFuture channelFuture = bootstrap.bind(9999).sync();
        channel = channelFuture.channel();
        channel.closeFuture().sync();
    }


    public void close() {
        if (boss != null)
            boss.shutdownGracefully().awaitUninterruptibly(15000);
        if (worker != null)
            worker.shutdownGracefully().awaitUninterruptibly(15000);
        LOGGER.info("NettyServer stopped...");
    }

    public Channel getChannel() {
        return channel;
    }
}
