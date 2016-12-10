package com.zhizus.forest.core;

import com.zhizus.forest.common.annotation.Var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dempe on 2016/12/7.
 */
public class MethodParam {

    private final static Map<Method, String[]> paramCacheMap = new ConcurrentHashMap<Method, String[]>();

    public static String[] getParameterNames(Method method) {
        // 方法参数名称缓存中获取
        String[] parameterNames = paramCacheMap.get(method);
        if (parameterNames == null) {
            parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                for (int i = 0; i < parameterAnnotation.length; i++) {
                    if (parameterAnnotation[i].annotationType() == Var.class) {
                        String value = ((Var) parameterAnnotation[i]).value();
                        if (StringUtils.isNotBlank(value)) {
                            parameterNames[i] = value;
                        }
                    }
                }
            }
            // set cache
            paramCacheMap.put(method, parameterNames);
        }
        return parameterNames;
    }

    public static Class<?>[] getParameterClazz(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes;

    }

    public static Object[] getParameterValuesByMap(String[] parameterNames, Method method, Map<String, List<String>> params) {
        Object[] paramTarget = null;
        if (parameterNames != null) {
            paramTarget = new Object[parameterNames.length];
            Type[] type = method.getGenericParameterTypes();
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];
                String value = getStringParameter(params, parameterName);
                if (Integer.class == type[i] || StringUtils.equals(type[i].toString(), "int")) {
                    paramTarget[i] = Integer.parseInt(value);
                } else if (String.class == type[i]) {
                    paramTarget[i] = value;
                } else if (Boolean.class == type[i] || StringUtils.equals(type[i].toString(), "boolean")) {
                    paramTarget[i] = Boolean.parseBoolean(value);
                } else if (Long.class == type[i] || StringUtils.equals(type[i].toString(), "long")) {
                    paramTarget[i] = Long.parseLong(value);
                } else if (Short.class == type[i] || StringUtils.equals(type[i].toString(), "short")) {
                    paramTarget[i] = Short.parseShort(value);
                } else if (Double.class == type[i] || StringUtils.equals(type[i].toString(), "double")) {
                    paramTarget[i] = Double.parseDouble(value);
                } else if (Float.class == type[i] || StringUtils.equals(type[i].toString(), "float")) {
                    paramTarget[i] = Float.parseFloat(value);
                }

            }
        }
        return paramTarget;
    }


    public static String getStringParameter(Map<String, List<String>> params, String param) {
        List<String> paramList = params.get(param);
        if (paramList != null && paramList.size() > 0) {
            return paramList.get(0);
        }
        return null;
    }


}
