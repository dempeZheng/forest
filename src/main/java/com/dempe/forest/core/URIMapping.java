package com.dempe.forest.core;

import com.dempe.forest.core.annotation.Action;
import com.dempe.forest.core.annotation.URI;
import com.dempe.forest.core.invoker.ActionMethod;
import com.dempe.forest.core.invoker.InvokerWrapper;
import com.google.common.collect.Maps;
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
public class URIMapping {

    private final static Logger LOGGER = LoggerFactory.getLogger(URIMapping.class);

    private ApplicationContext context;

    private Map<String, ActionMethod> mapping = Maps.newConcurrentMap();

    public ActionMethod getInvokerWrapperByURI(String uri) {
        return mapping.get(uri);
    }

    public URIMapping(ApplicationContext context) {
        this.context = context;
        initMapping();
    }


    public void initMapping() {
        String[] beanNamesForAnnotation = context.getBeanNamesForAnnotation(Action.class);
        for (String actionBeanName : beanNamesForAnnotation) {
            Object actionBean = context.getBean(actionBeanName);
            LOGGER.info("registered action  :{} ", actionBeanName);
            for (Method method : actionBean.getClass().getDeclaredMethods()) {
                if (method.getModifiers() == Modifier.PUBLIC) {
                    URI refs = method.getAnnotation(URI.class);
                    if (refs != null) {
                        String pathVal = String.valueOf(refs.value());
                        if (StringUtils.isBlank(pathVal)) {
                            pathVal = method.getName();
                        }
                        String uri = buildURI(actionBeanName, pathVal);
                        if (mapping.containsKey(uri)) {
                            LOGGER.warn("Method:{} declares duplicated jsonURI:{}, previous one will be overwritten", method, uri);
                        }
                        makeAccessible(method);
                        /**
                         * 从spring ioc容器中获取相应的bean
                         */

                        ActionMethod actionMethod = new ActionMethod(actionBean, method);
                        LOGGER.info("[REQUEST MAPPING] = {}, uri = {}", actionBeanName, uri);
                        mapping.put(uri, actionMethod);
                    }
                }
            }
        }


    }

    private String buildURI(String actionBeanName, String uri) {
        return "/" + actionBeanName + "/" + uri;
    }


    protected void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }
}
