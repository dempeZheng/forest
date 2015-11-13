package com.dempe.ketty.srv.http;

import com.dempe.ketty.srv.exception.ModelConvertJsonException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/2
 * Time: 21:04
 * To change this template use File | Settings | File Templates.
 */
public class HttpDispatcherHandler extends HttpStaticFileServerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpDispatcherHandler.class);

    private HttpServerContext context;


    public HttpDispatcherHandler(HttpServerContext context) {
        this.context = context;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IllegalAccessException,
            ModelConvertJsonException, InvocationTargetException {

        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }

    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws IllegalAccessException,
            ModelConvertJsonException, InvocationTargetException {

        if (is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
        ctx.executor().execute(new HttpWorkTask(ctx, req, context));

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {

        Channel ch = ctx.channel();

        // Prevent recursion when the client close the connection during a write operation. In that
        // scenario the sendError will be invoked, but will fail since the channel has already been closed
        // For an unknown reason,
        if (ch.attr(ATTACHMENT) != null && Error.class.isAssignableFrom(ch.attr(ATTACHMENT).get().getClass())) {
            return;
        }

        Throwable cause = t.getCause();
        if (cause instanceof TooLongFrameException) {
            sendError(ctx, BAD_REQUEST, null);
            return;
        }

        ch.attr(ATTACHMENT).set(new Error());
        if (ch.isOpen()) {
            sendError(ctx, INTERNAL_SERVER_ERROR, null);
        }

        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR, null);
        }
    }


}

class HttpWorkTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpWorkTask.class);

    private ChannelHandlerContext ctx;
    private FullHttpRequest req;
    private HttpServerContext context;

    public HttpWorkTask(ChannelHandlerContext ctx, FullHttpRequest req, HttpServerContext context) {
        this.context = context;
        this.req = req;
        this.context = context;
    }

    @Override
    public void run() {

        try {
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
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (ModelConvertJsonException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}
