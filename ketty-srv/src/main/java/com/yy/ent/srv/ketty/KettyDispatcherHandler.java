package com.yy.ent.srv.ketty;

import com.yy.ent.common.Constants;
import com.yy.ent.common.MetricThread;
import com.yy.ent.protocol.KettyRequest;
import com.yy.ent.protocol.KettyResponse;
import com.yy.ent.protocol.Response;
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
public class KettyDispatcherHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(KettyDispatcherHandler.class);

    private KettyServerContext context;

    private static MetricThread metric = new MetricThread("server");

    public KettyDispatcherHandler(KettyServerContext context) {
        this.context = context;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        metric.increment();
        KettyRequest req = (KettyRequest) msg;

        context.setReqCxt(req, ctx);
        KettyActionTack tack = new KettyActionTack(context);
        KettyResponse response = tack.act(req);
        context.removeReqCtx();

        LOGGER.debug("req:", req.toString());
        if (response != null) {
//            // 写入的时候已经release msg 无需显示的释放
            ctx.writeAndFlush(response);
        }
    }


}
