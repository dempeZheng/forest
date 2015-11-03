package com.yy.ent.srv.core;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:45
 * To change this template use File | Settings | File Templates.
 */
public class ServerContext {

    private RequestMapping mapping = new RequestMapping();

    private ChannelHandlerContext ctx;

    private Object request;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public ActionMethod get(String uri) {
        return mapping.tack(uri);
    }


    @Override
    public String toString() {
        return "ServerContext{" +
                "mapping=" + mapping +
                ", ctx=" + ctx +
                ", request=" + request +
                '}';
    }
}
