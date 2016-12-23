/*
 * #%L
 * %%
 * Copyright (C) 2016, Thiago Gutenberg Carvalho da Costa.
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the Thiago Gutenberg Carvalho da Costa. nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package com.zhizus.forest.support.jersey;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.header.HttpDateFormat;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.container.*;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Thiago Gutenberg Carvalho da Costa
 */
@ChannelHandler.Sharable
public final class NettyHandlerContainer extends ChannelInboundHandlerAdapter implements ContainerListener {

    public static final String PROPERTY_BASE_URI = "com.sun.jersey.server.impl.container.httpserver.baseUri";

    private volatile WebApplication webApplication;
    private volatile String baseUri;

    public NettyHandlerContainer(WebApplication webApplication, ResourceConfig resourceConfig) {
        this.webApplication = webApplication;
        this.baseUri = (String) resourceConfig.getProperty(PROPERTY_BASE_URI);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;

            if (HttpUtil.is100ContinueExpected(req)) {
                ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
            }

            final URI baseUri = getBaseUri(ctx, req);
            final URI requestUri = baseUri.resolve(req.uri());
            final InBoundHeaders inBoundHeaders = getHeaders(req);
            final InputStream entityStream = new ByteBufInputStream(req.content());

            /**
             * Create a new container request.
             * <p/>
             * The base URI and the request URI must contain the same scheme, user info,
             * host and port components.
             * <p/>
             * The base URI must not contain the query and fragment components. The
             * encoded path component of the request URI must start with the encoded
             * path component of the base URI. The encoded path component of the base
             * URI must end in a '/' character.
             *
             * @param webApplication the web application
             * @param httpMethod     the HTTP method
             * @param baseUri        the base URI of the request
             * @param requestUri     the request URI
             * @param inBoundHeaders the request headers
             * @param entityStream   the InputStream of the request entity
             */
            final ContainerRequest cRequest = new ContainerRequest(
                    webApplication,
                    req.method().name(),
                    baseUri,
                    requestUri,
                    inBoundHeaders,
                    entityStream
            );

            webApplication.handleRequest(cRequest, new ResponseWriter(ctx, HttpUtil.isKeepAlive(req), req.protocolVersion()));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.copiedBuffer(getStackTraceAsString(cause) + "\r\n", CharsetUtil.UTF_8)));
        ctx.close();
    }

    /**
     * Returns the {@link Throwable}'s stack trace information as {@link String}.
     * The result {@link String} format will be the same as reported by {@link Throwable#printStackTrace()}.
     *
     * @param t the error cause
     * @return the the error cause stacktrace information as {@link String}.
     */
    private String getStackTraceAsString(final Throwable t) {
        final StringWriter stringWriter = new StringWriter(2048);
        final PrintWriter pw = new PrintWriter(stringWriter);
        t.printStackTrace(pw);
        pw.close();
        return stringWriter.toString();
    }

    private URI getBaseUri(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (baseUri != null) {
            // Ensure that the base path ends with a '/'
            return baseUri.trim().endsWith("/") ? URI.create(baseUri) : URI.create(baseUri + "/");
        }
        final String reqUri = req.uri();
        final String protocol = req.protocolVersion().protocolName().toLowerCase();
        final String basePath = reqUri.split("/")[reqUri.indexOf('/') + 1];
        String host = req.headers().get(HttpHeaderNames.HOST);
        if (host == null) {
            InetSocketAddress address = (InetSocketAddress) ctx.channel().localAddress();
            host = address.getHostName() + ":" + address.getPort();
        }
        return URI.create(String.format("%s://%s/%s/", protocol, host, basePath));
    }

    private InBoundHeaders getHeaders(HttpRequest request) {
        InBoundHeaders inBoundHeaders = new InBoundHeaders();
        HttpHeaders httpHeaders = request.headers();
        for (String name : httpHeaders.names()) {
            inBoundHeaders.put(name, httpHeaders.getAll(name));
        }
        return inBoundHeaders;
    }

    @Override
    public void onReload() {
        WebApplication oldApplication = webApplication;
        webApplication = webApplication.clone();
        if (webApplication.getFeaturesAndProperties() instanceof ReloadListener) {
            ((ReloadListener) webApplication.getFeaturesAndProperties()).onReload();
        }
        oldApplication.destroy();
    }

    private final static class ResponseWriter implements ContainerResponseWriter {

        private final boolean keepAlive;
        private final HttpVersion httpVersion;
        private final ChannelHandlerContext ctx;

        private FullHttpResponse httpResponse;

        public ResponseWriter(ChannelHandlerContext ctx, boolean keepAlive, HttpVersion httpVersion) {
            this.ctx = ctx;
            this.keepAlive = keepAlive;
            this.httpVersion = httpVersion;
        }

        @Override
        public OutputStream writeStatusAndHeaders(long contentLength, ContainerResponse cResponse) throws IOException {
            httpResponse = new DefaultFullHttpResponse(httpVersion, HttpResponseStatus.valueOf(cResponse.getStatus()));

            for (Map.Entry<String, List<Object>> e : cResponse.getHttpHeaders().entrySet()) {
                List<String> values = new ArrayList<String>();
                for (Object v : e.getValue()) {
                    values.add(ContainerResponse.getHeaderValue(v));
                }
                httpResponse.headers().set(e.getKey(), values);
            }

            httpResponse.headers().add(HttpHeaderNames.DATE, HttpDateFormat.getPreferedDateFormat().format(new Date()));

            final boolean isContentLengthUnknown = contentLength < 0 &&
                    !cResponse.getHttpHeaders().containsKey(HttpHeaderNames.CONTENT_LENGTH) &&
                    !HttpUtil.isContentLengthSet(httpResponse);

            if (isContentLengthUnknown && keepAlive && httpVersion == HttpVersion.HTTP_1_1) {
                HttpUtil.setTransferEncodingChunked(httpResponse, true);
//                ctx.write(httpResponse);
                // TODO return chunked output stream ?
            } else if (isContentLengthUnknown) {
                if (cResponse.getEntityType() == String.class) {
                    String entity = (String) cResponse.getEntity();
                    byte[] bytes = entity.getBytes(CharsetUtil.UTF_8);
                    HttpUtil.setContentLength(httpResponse, bytes.length);
//                    ByteBuf byteBuf = Unpooled.copiedBuffer(entity, CharsetUtil.UTF_8);
//                    HttpHeaders.setContentLength(httpResponse, byteBuf.readableBytes());
                }
            } else {
                long length = contentLength > 0 ? contentLength : (Long) cResponse.getHttpHeaders().getFirst(HttpHeaderNames.CONTENT_LENGTH.toString());
                HttpUtil.setContentLength(httpResponse, length);
            }

            return new ByteBufOutputStream(httpResponse.content());
        }

        @Override
        public void finish() throws IOException {
            if (keepAlive) {
                httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
                ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}
