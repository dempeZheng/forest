package com.zhizus.forest.common.config;

import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Dempe on 2016/12/9.
 */
public class ServiceExportConfig {

    private String serviceName;// 这个值需要从接口层注解读取

    private int port;

    private Map<Method, MethodExportConfig> methodExportConfigMap = Maps.newConcurrentMap();


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void register(Method method, MethodExportConfig methodExportConfig) {
        methodExportConfigMap.put(method, methodExportConfig);
    }

    public String getMethodName(Method method) {
        MethodExportConfig methodExportConfig = methodExportConfigMap.get(method);
        return methodExportConfig == null ? null : methodExportConfig.getMethodName();
    }


}
