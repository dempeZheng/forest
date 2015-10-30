package com.yy.ent.srv.core;

import com.yy.ent.common.MetricThread;
import com.yy.ent.protocol.GardenReq;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherHandler.class);

    private ServerContext context;

    private static MetricThread metric = new MetricThread("server");

    private ExecutorService executorService = null;

    private final static int DEF_THREAD_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    public DispatcherHandler(ServerContext context) {
        this.context = context;
        this.executorService = Executors.newFixedThreadPool(DEF_THREAD_SIZE, new DefaultThreadFactory("METHOD_TASK"));

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        metric.increment();
        GardenReq req = (GardenReq) msg;
        LOGGER.info("req:", req.toString());
        executorService.submit(new MethodInvokerTask(ctx, context, req));
    }


}
