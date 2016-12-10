package com.zhizus.forest.common.config;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ServiceProviderConfig {

    private String serviceName;

    private int connectionTimeout;

    private Map<String, MethodConfig> methodConfigMap = Maps.newConcurrentMap();

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, MethodConfig> getMethodConfigMap() {
        return methodConfigMap;
    }

    public void setMethodConfigMap(Map<String, MethodConfig> methodConfigMap) {
        this.methodConfigMap = methodConfigMap;
    }

    public MethodConfig getMethodConfig(String methodName) {
        return methodConfigMap.get(methodName);
    }

    public MethodConfig registerMethodConfig(String methodName, MethodConfig methodConfig) {
        return methodConfigMap.put(methodName, methodConfig);
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public static class Builder {
        private ServiceProviderConfig config;

        public static Builder newBuilder() {
            Builder builder = new Builder();
            builder.config = new ServiceProviderConfig();
            return builder;
        }

        public ServiceProviderConfig build() {
            return config;
        }

        public Builder withConnectionTimeout(int connectionTimeout) {
            config.setConnectionTimeout(connectionTimeout);
            return this;
        }

        public Builder withMethodConfig(String methodName, MethodConfig methodConfig) {
            config.registerMethodConfig(methodName, methodConfig);
            return this;
        }

    }
}
