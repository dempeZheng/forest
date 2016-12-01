package com.dempe.forest.core;

import com.dempe.forest.core.annotation.Action;
import com.dempe.forest.core.annotation.Export;
import com.dempe.forest.core.annotation.Interceptor;
import com.dempe.forest.core.annotation.Rate;
import com.dempe.forest.core.interceptor.InvokerInterceptor;
import com.dempe.forest.core.invoker.ActionMethod;
import com.dempe.forest.core.invoker.MethodParam;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 9:59
 * To change this template use File | Settings | File Templates.
 */
public class AnnotationRouterMapping {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnnotationRouterMapping.class);

    private ApplicationContext context;

    private Map<String, ActionMethod> mapping = Maps.newConcurrentMap();

    public ActionMethod getInvokerWrapperByURI(String uri) {
        return mapping.get(uri);
    }

    public AnnotationRouterMapping(ApplicationContext context) {
        this.context = context;
        initMapping();
    }


    public void initMapping() {
        // 获取spring中Action注解的bean
        String[] beanNamesForAnnotation = context.getBeanNamesForAnnotation(Action.class);
        for (String actionBeanName : beanNamesForAnnotation) {
            Object actionBean = context.getBean(actionBeanName);
            for (Method method : actionBean.getClass().getDeclaredMethods()) {
                if (method.getModifiers() == Modifier.PUBLIC) {
                    Export refs = method.getAnnotation(Export.class);
                    if (refs != null) {
                        String pathVal = String.valueOf(refs.uri());
                        if (StringUtils.isBlank(pathVal)) {
                            pathVal = method.getName();
                        }
                        String uri = ForestUtil.buildURI(actionBeanName, pathVal);
                        if (mapping.containsKey(uri)) {
                            LOGGER.warn("Method:{} declares duplicated uri:{}, previous one will be overwritten", method, uri);
                        }
                        makeAccessible(method);
                        ActionMethod actionMethod = new ActionMethod(actionBean, method);
                        String[] parameterNames = MethodParam.getParameterNames(method);
                        actionMethod.setArgsName(parameterNames);
                        LOGGER.info("Register router mapping : {}, uri : {}", actionBeanName, uri);

                        // Interceptor
                        Interceptor interceptor = method.getAnnotation(Interceptor.class);
                        String id = interceptor.id();
                        if (Strings.isNullOrEmpty(id)) {
                            LOGGER.warn("Interceptor id is empty !");
                        }else {
                            for (String beanId : id.split(",")) {
                                InvokerInterceptor invokerInterceptor = (InvokerInterceptor) context.getBean(beanId);
                                actionMethod.addInterceptorList(invokerInterceptor);
                            }
                        }
                        // Rate
                        Rate rate = method.getAnnotation(Rate.class);
                        int value = rate.value();
                        if(value>0){
                            actionMethod.setRateLimiter(RateLimiter.create(value));
                        }else {
                            LOGGER.warn("Rate value < 0 !");
                        }
                        mapping.put(uri, actionMethod);
                    }
                }
            }
        }


    }


    protected void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }
}
