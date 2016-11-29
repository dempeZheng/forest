package com.dempe.forest.core.invoker;

import com.dempe.forest.core.interceptor.InvokerInterceptor;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class ActionMethod {

    private Object target;
    private Method method;
    private RateLimiter rateLimiter;
    private List<InvokerInterceptor> interceptorList;

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
        boolean condition = interceptorList != null && interceptorList.size() > 0;
        if (condition) {
            Iterator<InvokerInterceptor> iterator = interceptorList.iterator();
            boolean invokeBeforeReturn = true;
            while (iterator.hasNext() && invokeBeforeReturn) {
                InvokerInterceptor next = iterator.next();
                invokeBeforeReturn = next.before(args);
            }
        }
        Object result = call(args);

        if (condition) {
            Iterator<InvokerInterceptor> iterator = interceptorList.iterator();
            boolean invokeAfterReturn = true;
            while (iterator.hasNext() && invokeAfterReturn) {
                InvokerInterceptor next = iterator.next();
                invokeAfterReturn = next.after(result);
            }
        }
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

    public List<InvokerInterceptor> getInterceptorList() {
        return interceptorList;
    }

    public void setInterceptorList(List<InvokerInterceptor> interceptorList) {
        this.interceptorList = interceptorList;
    }

    /**
     * 添加拦截器
     *
     * @param invokerInterceptor
     * @return
     */
    public boolean addInterceptorList(InvokerInterceptor invokerInterceptor) {
        if (interceptorList == null) {
            interceptorList = Lists.newArrayList();
        }
        return interceptorList.add(invokerInterceptor);
    }
}
