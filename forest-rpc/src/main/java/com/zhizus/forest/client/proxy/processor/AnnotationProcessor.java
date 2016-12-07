package com.zhizus.forest.client.proxy.processor;

import com.zhizus.forest.common.config.ServiceConfig;

import java.lang.reflect.Method;

/**
 * Created by Dempe on 2016/12/7.
 */
public interface AnnotationProcessor {

    void process(String serviceName, Method method, ServiceConfig config);
}
