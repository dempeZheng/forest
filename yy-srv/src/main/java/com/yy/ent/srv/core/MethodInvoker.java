package com.yy.ent.srv.core;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.common.utils.LocalVariableTableParameterNameDiscoverer;
import com.yy.ent.mvc.anno.Param;
import com.yy.ent.srv.exception.JServerException;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/21
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
public class MethodInvoker {

    /**
     * 反射调用方法，根据方法参数自动注入value
     *
     * @param actionMethod
     * @param params
     * @return
     * @throws JServerException
     */
    public Object invoke(ActionMethod actionMethod, JSONObject params) throws JServerException {
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
        return actionMethod.call(paramTarget);
    }

}
