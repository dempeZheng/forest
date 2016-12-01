package com.dempe.forest.client.proxy;

import com.dempe.forest.transport.NettyClient;
import org.springframework.cglib.proxy.Enhancer;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/1
 * Time: 9:45
 * To change this template use File | Settings | File Templates.
 */
public class CglibProxy {

    public <T> T getProxy(Class<T> clz, NettyClient client) throws InterruptedException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clz);
        enhancer.setCallback(new ReferMethodInterceptor(clz, client));
        return (T) enhancer.create();
    }
}
