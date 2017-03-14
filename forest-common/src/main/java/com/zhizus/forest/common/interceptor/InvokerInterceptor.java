package com.zhizus.forest.common.interceptor;

import java.lang.reflect.Method;

/**
 * Created by Dempe on 2016/12/7.
 */
public interface InvokerInterceptor {

    boolean beforeInvoke(Object target, Method method, Object... args);

    Object processInvoke(Object target, Method method, Object... args);

    boolean afterInvoke(Object target, Method method, Object result);
}
