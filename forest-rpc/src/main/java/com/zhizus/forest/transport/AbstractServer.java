package com.zhizus.forest.transport;

import com.zhizus.forest.IRouter;
import com.zhizus.forest.common.config.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Dempe on 2016/12/22.
 */
public abstract class AbstractServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServer.class);

    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ServerBootstrap bootstrap;
    private Channel channel;
    private ServerConfig config;
    private int port;

    protected enum ServerState {Created, Starting, Started, Shutdown}

    protected final AtomicReference<ForestServer.ServerState> serverStateRef;

    public AbstractServer(final IRouter iRouter, ServerConfig config, int port) throws InterruptedException {
        this.port = port;
        this.config = config;
        serverStateRef = new AtomicReference<>(ForestServer.ServerState.Created);
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();

        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, config.soBacklog)
                .option(ChannelOption.SO_KEEPALIVE, config.soKeepAlive)
                .option(ChannelOption.TCP_NODELAY, config.tcpNoDelay)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(newChannelInitializer(iRouter));
    }

    public abstract ChannelInitializer<SocketChannel> newChannelInitializer(final IRouter iRouter);


    public ChannelFuture start() throws InterruptedException {
        if (!serverStateRef.compareAndSet(ForestServer.ServerState.Created, ForestServer.ServerState.Starting)) {
            throw new IllegalStateException("Server already started");
        }
        ChannelFuture channelFuture = bootstrap.bind(port);
        LOGGER.info("Server bind port:{}, soBacklog:{}, soKeepLive:{}, tcpNodDelay:{}", port,
                config.soBacklog, config.soKeepAlive, config.tcpNoDelay);

        serverStateRef.set(ForestServer.ServerState.Started); // It will come here only if this was the thread that transitioned to Starting
        channel = channelFuture.channel();
        channel.closeFuture();
        return channelFuture;
    }


    public void startAndWait() throws InterruptedException {
        start();
        try {
            waitTillShutdown();
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

    public void shutdown() throws InterruptedException {
        if (!serverStateRef.compareAndSet(ForestServer.ServerState.Started, ForestServer.ServerState.Shutdown)) {
            throw new IllegalStateException("The server is already shutdown.");
        } else {
            channel.close().sync();
        }
    }

    public void waitTillShutdown() throws InterruptedException {
        ForestServer.ServerState serverState = serverStateRef.get();
        switch (serverState) {
            case Created:
            case Starting:
                throw new IllegalStateException("Server not started yet.");
            case Started:
                channel.closeFuture().await();
                break;
            case Shutdown:
                // Nothing to do as it is already shutdown.
                break;
        }
    }

    public void close() {
        if (boss != null)
            boss.shutdownGracefully().awaitUninterruptibly(15000);
        if (worker != null)
            worker.shutdownGracefully().awaitUninterruptibly(15000);
        LOGGER.info("Server stopped...");
    }

}
