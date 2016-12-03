package com.dempe.forest.core.interceptor;

import com.google.common.collect.Lists;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/3 0003
 * Time: 下午 9:49
 * To change this template use File | Settings | File Templates.
 */
public class ChainInvokerInterceptor implements InvokerInterceptor {

    private List<InvokerInterceptor> interceptors = Lists.newArrayList();

    @Override
    public boolean beforeInvoke(Object target, Method method, Object... args) {
        Iterator<InvokerInterceptor> iterator = interceptors.iterator();
        boolean noInterrupt = true;
        while (iterator.hasNext() && true) {
            noInterrupt = iterator.next().beforeInvoke(target, method, args);
        }
        return noInterrupt;
    }

    @Override
    public Object processInvoke(Object target, Method method, Object... args) {
        Object result = null;
        for (InvokerInterceptor interceptor : interceptors) {
            Object ret = interceptor.processInvoke(target, method, args);
            if (ret != null) {
                result = ret;
            }
        }
        return result;
    }

    @Override
    public boolean afterInvoke(Object target, Method method, Object result) {
        Iterator<InvokerInterceptor> iterator = interceptors.iterator();
        boolean noInterrupt = true;
        while (iterator.hasNext() && true) {
            noInterrupt = iterator.next().afterInvoke(target, method, result);
        }
        return noInterrupt;
    }

    public boolean addInvokerInterceptor(InvokerInterceptor invokerInterceptor) {
        return interceptors.add(invokerInterceptor);
    }

    public void addInvokerInterceptor(int index, InvokerInterceptor invokerInterceptor) {
        interceptors.add(index, invokerInterceptor);
    }
}
