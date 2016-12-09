package com.zhizus.forest.client.proxy.processor;

import com.zhizus.forest.common.annotation.MethodProvider;
import com.zhizus.forest.common.annotation.ServiceExport;
import com.zhizus.forest.common.annotation.ServiceProvider;
import com.zhizus.forest.common.config.MethodExportConfig;
import com.zhizus.forest.common.config.ServiceExportConfig;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * Created by Dempe on 2016/12/9.
 */
public class ServiceExportProcessor extends AbstractAnnotationProcessor {

    @Override
    public void process(Class<?> serviceClass, Method method, ServiceExportConfig config) {
        ServiceExport serviceExport = serviceClass.getAnnotation(ServiceExport.class);
        if (serviceExport == null) {
            return;
        }
        Class<?>[] interfaces = serviceClass.getInterfaces();
        String serviceName = null;
        String methodName = null;
        for (Class<?> anInterface : interfaces) {
            ServiceProvider provider = anInterface.getAnnotation(ServiceProvider.class);
            serviceName = provider.serviceName();
            for (Method interfaceMethod : anInterface.getMethods()) {
                if (methodEquals(interfaceMethod, method)) {
                    MethodProvider methodProvider = interfaceMethod.getAnnotation(MethodProvider.class);
                    if (methodProvider == null) {
                        continue;
                    }
                    methodName = methodProvider.methodName();
                }
            }
        }
        config.setServiceName(serviceName);
        config.setPort(serviceExport.port());
        MethodExportConfig methodExportConfig = new MethodExportConfig();
        methodExportConfig.setMethodName(methodName);
        config.register(method, methodExportConfig);
    }


    public boolean methodEquals(Method interfaceMethod, Method method) {
        if (StringUtils.equals(method.getName(), interfaceMethod.getName())) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?>[] interfaceMethodParameterTypes = interfaceMethod.getParameterTypes();
            if (parameterTypes.length != interfaceMethodParameterTypes.length) {
                return false;
            }
            for (int i = 0; i < parameterTypes.length; i++) {
                if (!StringUtils.equals(parameterTypes[i].getName(), interfaceMethodParameterTypes[i].getName())) {
                    break;
                }
                return true;
            }
        }
        return false;
    }
}
