package com.yy.ent.srv.http;

import com.yy.ent.srv.KettyServer;
import com.yy.ent.srv.core.ActionMethod;
import com.yy.ent.srv.core.ServerContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 10:54
 * To change this template use File | Settings | File Templates.
 */
public class HttpServerContext extends ServerContext {

    private final static ThreadLocal<HttpRequestContext> contextThreadLocal = new ThreadLocal<HttpRequestContext>();

    public HttpServerContext(KettyServer.Builder builder) {
        super(builder);
    }


    public static void setReqCxt(HttpRequest request, ChannelHandlerContext ctx) {
        contextThreadLocal.set(new HttpRequestContext(request, ctx));
    }

    public static HttpRequestContext getReqCxt() {
        return contextThreadLocal.get();
    }

    public static void removeReqCtx() {
        contextThreadLocal.remove();
    }

    public ActionMethod tackAction(String uri) {
        return mapping.tack(uri);
    }


}

