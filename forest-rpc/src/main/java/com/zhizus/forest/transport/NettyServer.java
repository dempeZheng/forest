package com.zhizus.forest.transport;

import com.zhizus.forest.IRouter;
import com.zhizus.forest.codec.ForestDecoder;
import com.zhizus.forest.codec.ForestEncoder;
import com.zhizus.forest.common.config.ServerConfig;
import com.zhizus.forest.handler.ProcessorHandler;
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
 * Created by Dempe on 2016/12/9.
 */
public class NettyServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ServerBootstrap bootstrap;
    private Channel channel;
    private IRouter iRouter;
    private ServerConfig config;
    private int port;
    protected enum ServerState {Created, Starting, Started, Shutdown}
    protected final AtomicReference<ServerState> serverStateRef;


    public NettyServer(IRouter iRouter, ServerConfig config, int port) throws InterruptedException {
        this.iRouter = iRouter;
        this.port = port;
        this.config = config;
        serverStateRef = new AtomicReference<ServerState>(ServerState.Created);
    }


    public ChannelFuture doBind() throws InterruptedException {
        if (!serverStateRef.compareAndSet(ServerState.Created, ServerState.Starting)) {
            throw new IllegalStateException("Server already started");
        }
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();

        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, config.soBacklog)
                .option(ChannelOption.SO_KEEPALIVE, config.soKeepAlive)
                .option(ChannelOption.TCP_NODELAY, config.tcpNoDelay)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("decoder", new ForestDecoder());
                ch.pipeline().addLast("encoder", new ForestEncoder());
                ch.pipeline().addLast("processor", new ProcessorHandler(iRouter));
            }
        });

        ChannelFuture channelFuture = bootstrap.bind(port);
        LOGGER.info("NettyServer bind port:{}, soBacklog:{}, soKeepLive:{}, tcpNodDelay:{}", port,
                config.soBacklog, config.soKeepAlive, config.tcpNoDelay);

        serverStateRef.set(ServerState.Started); // It will come here only if this was the thread that transitioned to Starting
        channel = channelFuture.channel();
        channel.closeFuture();
        return channelFuture;
    }

    public void waitTillShutdown() throws InterruptedException {
        ServerState serverState = serverStateRef.get();
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
        LOGGER.info("NettyServer stopped...");
    }

}
