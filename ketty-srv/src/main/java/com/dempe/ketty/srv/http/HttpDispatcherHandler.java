package com.dempe.ketty.srv.http;

import com.dempe.ketty.srv.exception.ModelConvertJsonException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/2
 * Time: 21:04
 * To change this template use File | Settings | File Templates.
 */
public class HttpDispatcherHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpDispatcherHandler.class);

    private HttpServerContext context;

    public HttpDispatcherHandler(HttpServerContext context) {
        this.context = context;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IllegalAccessException,
            ModelConvertJsonException, InvocationTargetException {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            if (is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            HttpServerContext.setReqCxt(req, ctx);
            HttpActionTack tack = new HttpActionTack(context);
            FullHttpResponse response = tack.act(req);
            HttpServerContext.removeReqCtx();
            boolean keepAlive = HttpHeaders.isKeepAlive(req);
            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                ctx.writeAndFlush(response);
            }
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
