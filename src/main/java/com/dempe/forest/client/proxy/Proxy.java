package com.dempe.forest.client.proxy;

import com.dempe.forest.client.ChannelPool;
import org.springframework.cglib.proxy.Enhancer;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/1
 * Time: 9:45
 * To change this template use File | Settings | File Templates.
 */
public class Proxy {

    public static <T> T getCglibProxy(Class<T> clz, ChannelPool channelPool) throws InterruptedException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clz);
        enhancer.setCallback(new ReferMethodInterceptor(clz, channelPool));
        return (T) enhancer.create();
    }

}
