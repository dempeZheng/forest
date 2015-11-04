package com.dempe.ketty.srv.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */
public class HttpRequestContext {

    private HttpRequest request;
    private ChannelHandlerContext ctx;

    public HttpRequestContext(HttpRequest request, ChannelHandlerContext ctx) {
        this.request = request;
        this.ctx = ctx;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

}
