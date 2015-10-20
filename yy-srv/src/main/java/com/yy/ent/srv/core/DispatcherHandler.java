package com.yy.ent.srv.core;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.ioc.Param;
import com.yy.ent.srv.exception.JServerException;
import com.yy.ent.srv.method.ActionMethod;
import com.yy.ent.srv.utils.LocalVariableTableParameterNameDiscoverer;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherHandler.class);

    private ServerContext context;

    public DispatcherHandler(ServerContext context) {
        this.context = context;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject json = JSONObject.parseObject((String) msg);
        Long id = json.getLong("id");
        JSONObject params = json.getJSONObject("params");
        context.setRequestContext(params);
        dispatcher(json.getString("uri"));
        super.channelRead(ctx, msg);
    }

    private void dispatcher(String uri) throws JServerException {
        ActionMethod actionMethod = context.get(uri);
        if (actionMethod == null) {
            LOGGER.warn("[dispatcher]:not find uri {}", uri);
        }
        JSONObject requestParams = context.getRequestParams();
        invoke(actionMethod, requestParams);
    }

    public void invoke(ActionMethod actionMethod, JSONObject params) throws JServerException {
        Method method = actionMethod.getMethod();
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (int i = 0; i < parameterAnnotation.length; i++) {
                if (parameterAnnotation[i].annotationType() == Param.class) {
                    String value = ((Param) parameterAnnotation[i]).value();
                    if (StringUtils.isNotBlank(value)) {
                        parameterNames[i] = value;
                    }
                }
            }
        }

        Object[] paramTarget = null;
        if (parameterNames != null) {
            paramTarget = new Object[parameterNames.length];
            Type[] type = method.getGenericParameterTypes();
            for (int i = 0; i < parameterNames.length; i++) {
                if (Integer.class == type[i] || StringUtils.equals(type[i].toString(), "int")) {
                    paramTarget[i] = params.getInteger(parameterNames[i]);
                } else if (String.class == type[i]) {
                    paramTarget[i] = params.getString(parameterNames[i]);
                } else if (Boolean.class == type[i] || StringUtils.equals(type[i].toString(), "boolean")) {
                    paramTarget[i] = params.getBoolean(parameterNames[i]);
                } else if (Long.class == type[i] || StringUtils.equals(type[i].toString(), "long")) {
                    paramTarget[i] = params.getLong(parameterNames[i]);
                } else if (Short.class == type[i] || StringUtils.equals(type[i].toString(), "short")) {
                    paramTarget[i] = params.getShort(parameterNames[i]);
                } else if (Double.class == type[i] || StringUtils.equals(type[i].toString(), "double")) {
                    paramTarget[i] = params.getDouble(parameterNames[i]);
                } else if (Float.class == type[i] || StringUtils.equals(type[i].toString(), "float")) {
                    paramTarget[i] = params.getFloat(parameterNames[i]);
                }

            }
        }
        actionMethod.call(paramTarget);


    }


}
