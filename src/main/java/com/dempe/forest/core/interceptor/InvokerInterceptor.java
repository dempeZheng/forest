package com.dempe.forest.core.interceptor;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
public interface InvokerInterceptor {

    boolean before(Object target, Method method, Object... args);

    boolean after(Object target, Method method, Object result);
}
