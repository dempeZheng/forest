package com.zhizus.forest.transport;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import com.zhizus.forest.ActionMethod;
import com.zhizus.forest.IRouter;
import com.zhizus.forest.MethodParam;
import com.zhizus.forest.common.config.ServerConfig;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by Dempe on 2016/12/7.
 */
public class HttpForestServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpForestServer.class);
    HttpServerProvider provider = null;
    HttpServer httpServer = null;
    private IRouter mapping;
    private ServerConfig config;

    public HttpForestServer(IRouter mapping, ServerConfig config) {
        this.mapping = mapping;
        this.config = config;
        provider = HttpServerProvider.provider();

    }

    public void start() {
        try {
            httpServer = provider.createHttpServer(new InetSocketAddress(config.httpPort), config.httpBacklog);
            LOGGER.info("HttpForestServer start. bind port:{}, backlog:{}", config.httpPort, config.httpBacklog);
        } catch (IOException e) {
            LOGGER.warn("createHttpServer error", e);
            return;
        }
        httpServer.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                URI requestURI = httpExchange.getRequestURI();
                String uri = requestURI.toString();
                LOGGER.info("httpserverService path {} url :{}", requestURI.getPath(), uri);
                String ret = null;
                QueryStringDecoder decoder = new QueryStringDecoder(uri);
                String path = decoder.path();
                LOGGER.debug("path:{}", path);

                if ("/favicon.ico".equals(path)) {
                    return;
                }
                Map<String, List<String>> params = null;
                // TODO handler POST
                if (StringUtils.equals(httpExchange.getRequestMethod(), HttpMethod.POST.toString())) {
                    return;
                } else if (StringUtils.equals(httpExchange.getRequestMethod(), HttpMethod.GET.toString())) {
                    params = decoder.parameters();
                }
                ActionMethod actionMethod = mapping.router(path);
                String[] parameterNames = MethodParam.getParameterNames(actionMethod.getMethod());
                Object[] paramValues = MethodParam.getParameterValuesByMap(parameterNames, actionMethod.getMethod(), params);

                try {
                    ret = JSONObject.toJSONString(actionMethod.rateLimiterInvoker(paramValues));
                } catch (InvocationTargetException e) {
                    LOGGER.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                ret = StringUtils.defaultIfEmpty(ret, "null");
                byte[] bytes = ret.getBytes();
                Headers responseHeaders = httpExchange.getResponseHeaders();
                responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
                httpExchange.sendResponseHeaders(200, bytes.length); // 设置响应头属性及响应信息的长度
                OutputStream out = httpExchange.getResponseBody(); // 获得输出流
                out.write(bytes);
                out.flush();
                httpExchange.close();
            }

        });
        httpServer.setExecutor(null);
        httpServer.start();
        LOGGER.info("server started");
    }


}
