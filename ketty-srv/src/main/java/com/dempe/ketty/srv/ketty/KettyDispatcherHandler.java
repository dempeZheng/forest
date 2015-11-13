package com.dempe.ketty.srv.ketty;

import com.dempe.ketty.common.MetricThread;
import com.dempe.ketty.protocol.KettyRequest;
import com.dempe.ketty.protocol.KettyResponse;
import com.dempe.ketty.srv.exception.ModelConvertJsonException;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public class KettyDispatcherHandler extends ChannelHandlerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(KettyDispatcherHandler.class);

    private KettyServerContext context;

    private static MetricThread metric = new MetricThread("server");

    public KettyDispatcherHandler(KettyServerContext context) {
        this.context = context;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        metric.increment();
        KettyRequest req = (KettyRequest) msg;
        ctx.executor().submit(new KettyWorkTask(ctx, context, req));

    }


}

class KettyWorkTask implements Runnable {

    public static final Logger LOGGER = LoggerFactory.getLogger(KettyWorkTask.class);
    private ChannelHandlerContext ctx;
    private KettyRequest req;

    private KettyServerContext context;

    public KettyWorkTask(ChannelHandlerContext ctx, KettyServerContext context, KettyRequest req) {
        this.context = context;
        this.ctx = ctx;
        this.req = req;
    }


    @Override
    public void run() {
        try {
            KettyServerContext.setReqCxt(req, ctx);
            KettyActionTack tack = new KettyActionTack(context);
            KettyResponse response = tack.act(req);
            KettyServerContext.removeReqCtx();
            if (response != null) {
//            // 写入的时候已经release msg 无需显示的释放
                ctx.writeAndFlush(response);
            }else {
                ReferenceCountUtil.release(req);
            }
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ModelConvertJsonException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}
