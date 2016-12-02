package com.dempe.forest.transport;

import com.alibaba.fastjson.JSONObject;
import com.dempe.forest.AnnotationRouterMapping;
import com.dempe.forest.core.MethodParam;
import com.dempe.forest.core.invoker.ActionMethod;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
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
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/1
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public class HttpForestServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpForestServer.class);

    private AnnotationRouterMapping mapping;
    private ServerConfig config;
    HttpServerProvider provider = null;
    HttpServer httpServer = null;

    public HttpForestServer(AnnotationRouterMapping mapping, ServerConfig config) {
        this.mapping = mapping;
        this.config = config;
        provider = HttpServerProvider.provider();

    }

    public void start() {
        try {
            httpServer = provider.createHttpServer(new InetSocketAddress(config.httpPort()), config.httpBacklog());
            LOGGER.info("HttpForestServer start. bind port:{}, backlog:{}", config.httpPort(), config.httpBacklog());
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
                ActionMethod actionMethod = mapping.getInvokerWrapperByURI(path);
                String[] parameterNames = MethodParam.getParameterNames(actionMethod.getMethod());
                Object[] paramValues = MethodParam.getParameterValuesByMap(parameterNames, actionMethod.getMethod(), params);

                try {
                    ret = JSONObject.toJSONString(actionMethod.rateLimiterInvoker(paramValues));
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
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
