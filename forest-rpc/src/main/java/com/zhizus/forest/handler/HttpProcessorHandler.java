package com.zhizus.forest.handler;

import com.zhizus.forest.ActionMethod;
import com.zhizus.forest.IRouter;
import com.zhizus.forest.common.codec.Message;
import com.zhizus.forest.common.codec.Request;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by Dempe on 2016/12/22.
 */
public class HttpProcessorHandler extends HttpStaticFileServerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpProcessorHandler.class);

    private final IRouter iRouter;

    public HttpProcessorHandler(IRouter iRouter) {
        this.iRouter = iRouter;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IllegalAccessException, InvocationTargetException {

        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }

    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws IllegalAccessException, InvocationTargetException {

        if (HttpUtil.is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
        if (!req.getDecoderResult().isSuccess()) {
            new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST);
            return;
        }

        String uri = req.getUri();

        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        String path = decoder.path();
        LOGGER.debug("uri:{}", uri);

        if ("/favicon.ico".equals(path)) {
            new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
            return;
        }

        uri = StringUtils.substringBefore(uri, "?");
        Map<String, List<String>> params = null;
        // TODO handler POST
        if (req.method() == HttpMethod.POST) {
            HttpPostRequestDecoder postRequestDecoder = new HttpPostRequestDecoder(
                    new DefaultHttpDataFactory(false), req);
            List<InterfaceHttpData> q = postRequestDecoder.getBodyHttpDatas();
            new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN);
            return;

        } else if (req.getMethod() == HttpMethod.GET) {
            params = decoder.parameters();
        }
        ActionMethod actionMethod = iRouter.router(uri);
        if (actionMethod == null) {
            LOGGER.warn("[dispatcher]:not find uri {}", uri);
            new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
            return;
        }

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

class HttpInvokerRunnable implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(InvokerRunnable.class);

    private ChannelHandlerContext ctx;
    private ActionMethod actionMethod;
    private Message<Request> message;

    public HttpInvokerRunnable(ActionMethod actionMethod, Message<Request> message, ChannelHandlerContext ctx) {
        this.actionMethod = actionMethod;
        this.message = message;
        this.ctx = ctx;
    }

    @Override
    public void run() {

    }

}
