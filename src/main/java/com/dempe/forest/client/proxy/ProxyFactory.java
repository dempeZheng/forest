package com.dempe.forest.client.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
public interface ProxyFactory {

    <T> T getProxy(Class<T> clz, InvocationHandler invocationHandler);

}