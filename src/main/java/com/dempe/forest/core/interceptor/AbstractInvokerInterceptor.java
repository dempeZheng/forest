package com.dempe.forest.core.interceptor;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/3 0003
 * Time: 下午 10:09
 * To change this template use File | Settings | File Templates.
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
