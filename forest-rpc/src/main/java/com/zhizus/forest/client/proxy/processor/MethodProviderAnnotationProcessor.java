package com.zhizus.forest.client.proxy.processor;

import com.google.common.base.Strings;
import com.zhizus.forest.common.annotation.MethodProvider;
import com.zhizus.forest.common.config.MethodConfig;
import com.zhizus.forest.common.config.ServiceProviderConfig;

import java.lang.reflect.Method;

/**
 * Created by Dempe on 2016/12/7.
 */
public class MethodProviderAnnotationProcessor extends AbstractAnnotationProcessor {


    @Override
    public void process(String serviceName, Method method, ServiceProviderConfig config) {
        MethodProvider methodProvider = method.getAnnotation(MethodProvider.class);
        if (methodProvider != null) {
            String methodName = Strings.isNullOrEmpty(methodProvider.methodName()) ? method.getName() : methodProvider.methodName();

            MethodConfig methodConfig = MethodConfig.Builder.newBuilder()
                    .withSerializeType(methodProvider.serializeType())
                    .withTimeout(methodProvider.timeout())
                    .withCompressType(methodProvider.compressType())
                    .build();

            methodConfig.setServiceName(serviceName);
            methodConfig.setMethodName(methodName);
            //
            config.registerMethodConfig(methodName, methodConfig);

        }

    }
}
