package com.dempe.forest.transport;

import com.dempe.forest.conf.ServerConf;
import com.dempe.forest.core.handler.Handler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
public class NettyServer extends AbstractServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ServerBootstrap bootstrap;
    private io.netty.channel.Channel channel;

    private GlobalTrafficShapingHandler globalTrafficShapingHandler;

    /**
     * 业务处理handler
     *
     * @throws Exception
     */
    public NettyServer(ServerConf transportConf) throws Exception {
        super(transportConf);
    }

    protected void doBind() throws Throwable {
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        EventExecutorGroup trafficExecutorGroup = new DefaultEventExecutorGroup(1);
        globalTrafficShapingHandler = new GlobalTrafficShapingHandler(trafficExecutorGroup, 1000);
        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, getServerConf().getAccepts()).option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(globalTrafficShapingHandler);
                ch.pipeline().addLast("decoder", getCodec().getDecoder());
                ch.pipeline().addLast("encoder", getCodec().getEncoder());
                ch.pipeline().addLast("business_handler", new NettyHandler());
            }
        });

        try {
            ChannelFuture channelFuture = bootstrap.bind(getBindAddress()).sync();
            channel = channelFuture.channel();
            //			channel.closeFuture().sync();
            startMornitor();
        } finally {
            //close();
        }
    }

    private void startMornitor() {
        Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setName("netty mornitor");
                t.setDaemon(true);
                t.setPriority(Thread.MIN_PRIORITY);
                return t;
            }
        }).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                LOGGER.info(getBindAddress()
                                + " read speed: {} KB/S , write speed: {} KB/S, cumulative read: {} KB, cumulative write: {} KB, ",
                        globalTrafficShapingHandler.trafficCounter().lastReadThroughput() >> 10, globalTrafficShapingHandler
                                .trafficCounter().lastWriteThroughput() >> 10, globalTrafficShapingHandler.trafficCounter()
                                .cumulativeReadBytes() >> 10, globalTrafficShapingHandler.trafficCounter().cumulativeWrittenBytes() >> 10);
            }
        }, 1000, globalTrafficShapingHandler.trafficCounter().checkInterval(), TimeUnit.MILLISECONDS);
    }

    public void close() {
        if (boss != null)
            boss.shutdownGracefully().awaitUninterruptibly(15000);
        Handler handler = getHandler();
        if (handler != null) {
            handler.stop();
        }
        if (worker != null)
            worker.shutdownGracefully().awaitUninterruptibly(15000);
        LOGGER.info("NettyServer stopped...");
    }

    public io.netty.channel.Channel getChannel() {
        return channel;
    }
}
