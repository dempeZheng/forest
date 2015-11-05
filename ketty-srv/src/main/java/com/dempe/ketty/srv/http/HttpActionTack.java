package com.dempe.ketty.srv.http;

import com.dempe.ketty.srv.core.ActionMethod;
import com.dempe.ketty.srv.core.ActionTake;
import com.dempe.ketty.srv.exception.ModelConvertJsonException;
import com.dempe.ketty.srv.uitl.HttpResponseBuilder;
import com.dempe.ketty.srv.uitl.MethodInvoker;
import com.dempe.ketty.srv.uitl.MethodParam;
import com.dempe.ketty.srv.uitl.ResultConcert;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class HttpActionTack implements ActionTake<FullHttpResponse, FullHttpRequest> {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpActionTack.class);

    private HttpServerContext context;

    public HttpActionTack(HttpServerContext context) {
        this.context = context;
    }


    public FullHttpResponse act(FullHttpRequest request) throws InvocationTargetException, IllegalAccessException, ModelConvertJsonException {

        // Handle a bad request.
        if (!request.getDecoderResult().isSuccess()) {
            return new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST);
        }

        String uri = request.getUri();

        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        String path = decoder.path();
        LOGGER.debug("uri:{}", uri);

        if ("/favicon.ico".equals(path)) {
            return new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
        }

        uri = StringUtils.substringBefore(uri, "?");
        Map<String, List<String>> params = null;
        // TODO handler POST
        if (request.getMethod() == HttpMethod.POST) {
            HttpPostRequestDecoder postRequestDecoder = new HttpPostRequestDecoder(
                    new DefaultHttpDataFactory(false), request);
            List<InterfaceHttpData> q = postRequestDecoder.getBodyHttpDatas();
            return new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN);

        } else if (request.getMethod() == HttpMethod.GET) {
            params = decoder.parameters();
        }
        ActionMethod actionMethod = context.tackAction(uri);
        if (actionMethod == null) {
            LOGGER.warn("[dispatcher]:not find uri {}", uri);
            return new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
        }

        Method method = actionMethod.getMethod();

        String[] parameterNames = MethodParam.getParameterNames(method);
        Object[] paramValues = MethodParam.getParameterValuesByMap(parameterNames, method, params);
        Object result = MethodInvoker.interceptorInvoker(actionMethod, paramValues);
        return HttpResponseBuilder.buildResponse(ResultConcert.toJSONString(result));
    }


}
