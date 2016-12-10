package com.zhizus.forest.client.proxy.processor;

import com.zhizus.forest.common.config.ServiceExportConfig;
import com.zhizus.forest.common.config.ServiceProviderConfig;

import java.lang.reflect.Method;

/**
 * Created by Dempe on 2016/12/7.
 */
public interface IAnnotationProcessor {

    void process(String serviceName, Method method, ServiceProviderConfig config);

    void process(Class<?> serviceClass, Method method, ServiceExportConfig config);
}
