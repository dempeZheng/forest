package com.dempe.forest.support.spring;

import com.dempe.forest.ClientOptions;
import com.dempe.forest.client.proxy.RpcProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/5
 * Time: 10:44
 * To change this template use File | Settings | File Templates.
 */
public class ProxyFactoryBean implements FactoryBean<Object>, InitializingBean, DisposableBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProxyFactoryBean.class);

    private Class<?> serviceInterface;

    private RpcProxy rpcProxy;

    private Map<String, ClientOptions> methodConfMap;

    private Object proxyBean;


    @Override
    public void destroy() throws Exception {

    }

    @Override
    public Object getObject() throws Exception {
        return rpcProxy.getProxy(serviceInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    public Map<String, ClientOptions> getMethodConfMap() {
        return methodConfMap;
    }

    public void setMethodConfMap(Map<String, ClientOptions> methodConfMap) {
        this.methodConfMap = methodConfMap;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rpcProxy = new RpcProxy();
        if (methodConfMap != null) {
            for (Map.Entry<String, ClientOptions> stringMethodConfBeanEntry : methodConfMap.entrySet()) {
                if (stringMethodConfBeanEntry.getKey() == null) {
                    LOGGER.warn("methodName is null,methodConfBean:{}", stringMethodConfBeanEntry.getValue());
                }
                rpcProxy.setMethodOption(stringMethodConfBeanEntry.getKey(), stringMethodConfBeanEntry.getValue());
            }
        }
        proxyBean = new ProxyFactoryBean();
    }

    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }
}
