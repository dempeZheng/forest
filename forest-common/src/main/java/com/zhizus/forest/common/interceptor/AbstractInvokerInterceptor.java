package com.zhizus.forest.common.interceptor;

import java.lang.reflect.Method;

/**
 * Created by Dempe on 2016/12/7.
 */
public class AbstractInvokerInterceptor implements InvokerInterceptor {
    @Override
    public boolean beforeInvoke(Object target, Method method, Object... args) {
        return true;
    }

    @Override
    public Object processInvoke(Object target, Method method, Object... args) {
        return null;
    }

    @Override
    public boolean afterInvoke(Object target, Method method, Object result) {
        return true;
    }
}
