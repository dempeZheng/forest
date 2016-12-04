package com.dempe.forest;

import com.dempe.forest.client.proxy.ReferConfig;
import com.dempe.forest.core.annotation.MethodProvider;
import com.dempe.forest.core.annotation.ServiceProvider;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/4 0004
 * Time: 上午 11:07
 * To change this template use File | Settings | File Templates.
 */
public class RefConfMapping {

    private final static Logger LOGGER = LoggerFactory.getLogger(RefConfMapping.class);

    private final static Map<String, ReferConfig> refConMap = Maps.newConcurrentMap();

    public RefConfMapping() {
        initRefConfMap();
    }

    public void initRefConfMap() {
        String[] classesInPackage = PackageUtils.findClassesInPackage(".*");
        for (String clazzName : classesInPackage) {
            try {
                Class<?> aClass = Class.forName(clazzName);
                ServiceProvider serviceProvider = aClass.getAnnotation(ServiceProvider.class);
                if (serviceProvider == null) {
                    continue;
                }
                String serviceName = Strings.isNullOrEmpty(serviceProvider.serviceName())
                        ? aClass.getSimpleName() : serviceProvider.serviceName();
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    MethodProvider methodProvider = method.getAnnotation(MethodProvider.class);
                    if (methodProvider == null) {
                        continue;
                    }
                    String methodName = methodProvider.methodName();
                    if (Strings.isNullOrEmpty(serviceName) || Strings.isNullOrEmpty(methodName)) {
                        continue;
                    }
                    String key = ForestUtil.buildUri(serviceName, methodName);
                    refConMap.put(key, ReferConfig.makeReferConfigByAnnotation(aClass, method));
                }

            } catch (ClassNotFoundException e) {
                LOGGER.warn("class : {} not found", clazzName);
            }
        }

    }


    public void registerRefConfMap(ReferConfig referConfig) {
        if (referConfig.isInit()) {
            refConMap.put(ForestUtil.buildUri(referConfig.getServiceName(), referConfig.getMethodName()), referConfig);
        } else {
            LOGGER.warn("referConfig isInit false, not register!");
        }
    }

    public ReferConfig getRefConf(String serviceName, String methodName) {
        return refConMap.get(ForestUtil.buildUri(serviceName, methodName));

    }

}
