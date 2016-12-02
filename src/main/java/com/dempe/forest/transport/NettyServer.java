package com.dempe.forest.transport;

import com.dempe.forest.AnnotationRouterMapping;
import com.dempe.forest.StandardThreadExecutor;
import com.dempe.forest.codec.ForestDecoder;
import com.dempe.forest.codec.ForestEncoder;
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
    private ServerConfig config;


    public NettyServer(AnnotationRouterMapping mapping, ServerConfig config) throws InterruptedException {
        this.uriMapping = mapping;
        this.config = config;
    }


    public ChannelFuture doBind() throws InterruptedException {
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        if (executor == null) {
            executor = new StandardThreadExecutor(config.coreThread(), config.maxThreads());
        }
        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, config.soBacklog())
                .option(ChannelOption.SO_KEEPALIVE, config.soKeepAlive())
                .option(ChannelOption.TCP_NODELAY, config.tcpNoDelay())
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("decoder", new ForestDecoder());
                ch.pipeline().addLast("encoder", new ForestEncoder());
                ch.pipeline().addLast("processor", new ProcessorHandler(uriMapping, executor));
            }
        });

        ChannelFuture channelFuture = bootstrap.bind(config.port());
        LOGGER.info("NettyServer bind port:{}, soBacklog:{}, soKeepLive:{}, tcpNodDelay:{}", config.port(),
                config.soBacklog(), config.soKeepAlive(), config.tcpNoDelay());
        channel = channelFuture.channel();
        channel.closeFuture();
        return channelFuture;
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
