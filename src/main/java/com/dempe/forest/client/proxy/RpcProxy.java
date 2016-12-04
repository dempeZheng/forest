package com.dempe.forest.client.proxy;

import com.dempe.forest.RefConfMapping;
import com.dempe.forest.core.annotation.ServiceProvider;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/4 0004
 * Time: 下午 2:30
 * To change this template use File | Settings | File Templates.
 */
public class RpcProxy {

    private final static Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);

    private RefConfMapping refConfMapping;

    public RpcProxy(RefConfMapping refConfMapping) {
        this.refConfMapping = refConfMapping;
    }
    public RpcProxy(){

        refConfMapping = new RefConfMapping();
    }

    public <T> T getProxy(Class<T> clazz) {
        ServiceProvider serviceProvider = clazz.getAnnotation(ServiceProvider.class);
        if (serviceProvider == null) {
            LOGGER.warn("cannot getProxy for class:{}", clazz);
            return null;
        }
        String serviceName = Strings.isNullOrEmpty(serviceProvider.serviceName()) ? clazz.getSimpleName() : serviceProvider.serviceName();
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, new ReferInvocationHandler(refConfMapping, serviceName));
    }

    public RefConfMapping getRefConfMapping() {
        return refConfMapping;
    }


    public RpcProxy setRefConfMapping(RefConfMapping refConfMapping) {
        this.refConfMapping = refConfMapping;
        return this;
    }
    public RpcProxy registerReferConfig(ReferConfig referConfig){
        refConfMapping.registerRefConfMap(referConfig);
        return this;


    }
}
