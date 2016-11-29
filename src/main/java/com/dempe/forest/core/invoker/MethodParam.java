package com.dempe.forest.core.invoker;

import com.dempe.forest.core.annotation.Param;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/3
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
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
                    if (parameterAnnotation[i].annotationType() == Param.class) {
                        String value = ((Param) parameterAnnotation[i]).value();
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



}
