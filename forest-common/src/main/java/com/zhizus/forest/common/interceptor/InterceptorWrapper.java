package com.zhizus.forest.common.interceptor;

public class InterceptorWrapper {
    public InvokerInterceptor interceptor;
    public String autoMatch;
    public String excludes;

    public InterceptorWrapper(InvokerInterceptor interceptor, String autoMatch, String excludes) {
        this.interceptor = interceptor;
        this.autoMatch = autoMatch;
        this.excludes = excludes;
    }
}
