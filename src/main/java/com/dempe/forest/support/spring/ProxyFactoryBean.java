package com.dempe.forest.support.spring;

import com.dempe.forest.MethodProviderConf;
import com.dempe.forest.client.proxy.RpcProxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
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
public class ProxyFactoryBean implements FactoryBean<Object>, InitializingBean, MethodInterceptor, DisposableBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProxyFactoryBean.class);

    private Class<?> serviceInterface;

    private RpcProxy rpcProxy;

    private Map<String, MethodProviderConf> methodConfMap;

    private Object proxyBean;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return rpcProxy.invoke(proxyBean, invocation.getMethod(), invocation.getArguments());
    }

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

    public Map<String, MethodProviderConf> getMethodConfMap() {
        return methodConfMap;
    }

    public void setMethodConfMap(Map<String, MethodProviderConf> methodConfMap) {
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
            for (Map.Entry<String, MethodProviderConf> stringMethodConfBeanEntry : methodConfMap.entrySet()) {
                if (stringMethodConfBeanEntry.getKey() == null) {
                    LOGGER.warn("methodName is null,methodConfBean:{}", stringMethodConfBeanEntry.getValue());
                }
                rpcProxy.setMethodProviderConfig(stringMethodConfBeanEntry.getKey(), stringMethodConfBeanEntry.getValue());
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
