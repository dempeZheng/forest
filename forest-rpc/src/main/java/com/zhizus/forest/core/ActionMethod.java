package com.zhizus.forest.core;

import com.zhizus.forest.core.interceptor.ChainInvokerInterceptor;
import com.zhizus.forest.core.interceptor.InvokerInterceptor;
import com.google.common.util.concurrent.RateLimiter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ActionMethod {

    private Object target;
    private String[] argsName;
    private Method method;
    private RateLimiter rateLimiter;
    private String group;
    private ChainInvokerInterceptor chainInvokerInterceptor;

    public ActionMethod(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    public Object call(Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }

    /**
     * 服务限流
     *
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object rateLimiterInvoker(Object... args) throws InvocationTargetException, IllegalAccessException {
        if (rateLimiter != null) {
            rateLimiter.acquire();
        }
        return interceptorInvoker(args);
    }

    /**
     * 调用前后拦截器
     *
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object interceptorInvoker(Object... args) throws InvocationTargetException, IllegalAccessException {
        if (chainInvokerInterceptor == null) {
            return call(args);
        }
        chainInvokerInterceptor.beforeInvoke(target, method, args);
        Object result = chainInvokerInterceptor.processInvoke(target, method, args);
        if (result == null) {
            result = call(args);
        }
        chainInvokerInterceptor.afterInvoke(target, method, result);
        return result;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public void setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    /**
     * 添加拦截器
     *
     * @param invokerInterceptor
     * @return
     */
    public boolean addInterceptorList(InvokerInterceptor invokerInterceptor) {
        if (chainInvokerInterceptor == null) {
            chainInvokerInterceptor = new ChainInvokerInterceptor();
        }
        return chainInvokerInterceptor.addInvokerInterceptor(invokerInterceptor);
    }

    public String[] getArgsName() {
        return argsName;
    }

    public void setArgsName(String[] argsName) {
        this.argsName = argsName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
