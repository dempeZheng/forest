package com.dempe.ketty.srv.uitl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 10:14
 * To change this template use File | Settings | File Templates.
 */
public class HttpResponseBuilder {

    public static FullHttpResponse buildResponse(String result) {
        ByteBuf content = Unpooled.copiedBuffer(result, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, content);
        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

}
