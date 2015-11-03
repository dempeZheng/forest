package com.yy.ent.srv.core;

import com.yy.ent.common.Constants;
import com.yy.ent.common.MetricThread;
import com.yy.ent.protocol.KettyRequest;
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

    private static ExecutorService executorService = Executors.newFixedThreadPool(Constants.DEF_THREAD_NUM,
            new DefaultThreadFactory("METHOD_TASK"));

    public DispatcherHandler(ServerContext context) {
        this.context = context;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        metric.increment();
        KettyRequest req = (KettyRequest) msg;
        context.setCtx(ctx);
        context.setRequest(req);
        LOGGER.debug("req:", req.toString());
        executorService.submit(new MethodInvokerTask(context));
    }


}
