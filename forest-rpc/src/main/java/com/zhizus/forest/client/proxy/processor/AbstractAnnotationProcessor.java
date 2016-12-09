package com.zhizus.forest.client.proxy.processor;

import com.zhizus.forest.common.config.ServiceExportConfig;
import com.zhizus.forest.common.config.ServiceProviderConfig;

import java.lang.reflect.Method;

/**
 * Created by Dempe on 2016/12/9.
 */
public abstract class AbstractAnnotationProcessor implements IAnnotationProcessor {

    @Override
    public void process(String serviceName, Method method, ServiceProviderConfig config) {

    }

    @Override
    public void process(Class<?> serviceClass, Method method, ServiceExportConfig config) {

    }
}
