package com.dempe.ketty.srv.ketty;

import com.dempe.ketty.protocol.KettyRequest;
import com.dempe.ketty.srv.KettyServer;
import com.dempe.ketty.srv.core.ServerContext;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 13:47
 * To change this template use File | Settings | File Templates.
 */
public class KettyServerContext extends ServerContext {

    private final static ThreadLocal<KettyRequestContext> contextThreadLocal = new ThreadLocal<KettyRequestContext>();

    public KettyServerContext(KettyServer.Builder builder) {
        super(builder);
    }


    public static void setReqCxt(KettyRequest request, ChannelHandlerContext ctx) {
        contextThreadLocal.set(new KettyRequestContext(request, ctx));
    }

    public static KettyRequestContext getReqCxt() {
        return contextThreadLocal.get();
    }

    public static void removeReqCtx() {
        contextThreadLocal.remove();
    }


}
