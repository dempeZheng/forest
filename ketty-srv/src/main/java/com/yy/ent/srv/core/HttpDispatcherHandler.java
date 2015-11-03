package com.yy.ent.srv.core;

import com.yy.ent.srv.exception.JServerException;
import com.yy.ent.srv.exception.ModelConvertJsonException;
import com.yy.ent.srv.uitl.MethodParam;
import com.yy.ent.srv.uitl.ResultConcert;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
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

    private ServerContext context;

    public HttpDispatcherHandler(ServerContext context) {
        this.context = context;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            if (is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            String result = getResult(req);
            if (result == null) {
                LOGGER.warn("result is null");
                return;
            }
            boolean keepAlive = isKeepAlive(req);
            FullHttpResponse response = buildResponse(result);
            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                ctx.writeAndFlush(response);
            }
        }
    }

    private String getResult(HttpRequest request) throws JServerException, ModelConvertJsonException {
        String uri = request.getUri();
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        String path = decoder.path();
        LOGGER.debug("uri:{}", uri);

        if ("/favicon.ico".equals(path)) {
            return null;
        }

        uri = StringUtils.substringBefore(uri, "?");
        Map<String, List<String>> params = null;
        String methodType = request.getMethod().name();
        if ("POST".equals(methodType)) {
            // TODO

        } else if ("GET".equals(methodType)) {
            params = decoder.parameters();
        }
        ActionMethod actionMethod = context.get(uri);
        if (actionMethod == null) {
            LOGGER.warn("[dispatcher]:not find uri {}", uri);
            // TODO return 404
            return "404";
        }
        Object result = invoke(actionMethod, params);
        if (result == null) {
            // 当action method 返回是void的时候，不返回任何消息
            LOGGER.debug("actionMethod:{} return void.", actionMethod);
            return null;
        }
        return ResultConcert.toJSONString(result);


    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 反射调用方法，根据方法参数自动注入value
     *
     * @param actionMethod
     * @param params
     * @return
     * @throws com.yy.ent.srv.exception.JServerException
     */
    public Object invoke(ActionMethod actionMethod, Map<String, List<String>> params) throws JServerException {
        Method method = actionMethod.getMethod();
        String[] parameterNames = MethodParam.getParameterNames(method);
        Object[] paramTarget = MethodParam.getParameterValuesByMap(parameterNames, method, params);
        return actionMethod.call(paramTarget);
    }

    private FullHttpResponse buildResponse(String result) {
        ByteBuf content = Unpooled.copiedBuffer(result, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, content);
        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }


}
