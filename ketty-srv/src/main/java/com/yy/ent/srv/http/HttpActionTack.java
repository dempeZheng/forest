package com.yy.ent.srv.http;

import com.yy.ent.srv.core.ActionMethod;
import com.yy.ent.srv.core.ActionTake;
import com.yy.ent.srv.exception.ModelConvertJsonException;
import com.yy.ent.srv.uitl.HttpResponseBuilder;
import com.yy.ent.srv.uitl.MethodInvoker;
import com.yy.ent.srv.uitl.MethodParam;
import com.yy.ent.srv.uitl.ResultConcert;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class HttpActionTack implements ActionTake<FullHttpResponse, HttpRequest> {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpActionTack.class);


    private HttpServerContext context;

    public HttpActionTack(HttpServerContext context) {
        this.context = context;
    }


    public FullHttpResponse act(HttpRequest request) throws InvocationTargetException, IllegalAccessException, ModelConvertJsonException {
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
        ActionMethod actionMethod = context.tackAction(uri);
        if (actionMethod == null) {
            LOGGER.warn("[dispatcher]:not find uri {}", uri);
            // TODO return 404
            return HttpResponseBuilder.buildResponse("404");
        }

        Method method = actionMethod.getMethod();

        String[] parameterNames = MethodParam.getParameterNames(method);
        Object[] paramValues = MethodParam.getParameterValuesByMap(parameterNames, method, params);
        Object result = MethodInvoker.interceptorInvoker(actionMethod, paramValues);
        if (result != null) {
            return HttpResponseBuilder.buildResponse("404");
        }
        return HttpResponseBuilder.buildResponse(ResultConcert.toJSONString(result));
    }


}
