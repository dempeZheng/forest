package com.dempe.ketty.srv.ketty;

import com.dempe.ketty.protocol.KettyRequest;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class KettyRequestContext {
    private KettyRequest request;
    private ChannelHandlerContext ctx;

    public KettyRequestContext(KettyRequest request, ChannelHandlerContext ctx) {
        this.request = request;
        this.ctx = ctx;
    }

    public KettyRequest getRequest() {
        return request;
    }

    public void setRequest(KettyRequest request) {
        this.request = request;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
