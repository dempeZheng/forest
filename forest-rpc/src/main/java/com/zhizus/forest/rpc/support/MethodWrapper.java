package com.zhizus.forest.rpc.support;

import com.google.common.util.concurrent.RateLimiter;

import java.lang.reflect.Method;

/**
 * Created by dempezheng on 2017/6/30.
 */
public class MethodWrapper {
    private Method method;
    private RateLimiter rateLimiter;
    private String uri;
    private int priority;
    private String methodName;
    private boolean accessLogOn;
    private Class<?> returnType;

    public boolean isAccessLogOn() {
        return accessLogOn;
    }

    public MethodWrapper setAccessLogOn(boolean accessLogOn) {
        this.accessLogOn = accessLogOn;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public MethodWrapper setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public MethodWrapper setMethod(Method method) {
        this.method = method;
        return this;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public MethodWrapper setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public MethodWrapper setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public MethodWrapper setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public MethodWrapper setReturnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }
}
