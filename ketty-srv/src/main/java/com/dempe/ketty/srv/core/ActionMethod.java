package com.dempe.ketty.srv.core;

import com.dempe.ketty.srv.interceptor.KettyInterceptor;
import com.google.common.util.concurrent.RateLimiter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/15
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class ActionMethod {


    private Object target;

    private Method method;

    private List<KettyInterceptor> interceptorList = new ArrayList<KettyInterceptor>();

    private RateLimiter rateLimiter = null;


    /**
     * @param target
     * @param method
     */
    public ActionMethod(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    public Object call(Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public List<KettyInterceptor> getInterceptorList() {
        return interceptorList;
    }

    public void addInterceptor(KettyInterceptor interceptor) {
        interceptorList.add(interceptor);
    }


    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public void setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public String toString() {
        return "ActionMethod{" +
                "target=" + target +
                ", method=" + method +
                ", interceptorList=" + interceptorList +
                '}';
    }
}
