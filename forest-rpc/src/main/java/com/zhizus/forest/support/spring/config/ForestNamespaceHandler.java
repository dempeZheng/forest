package com.zhizus.forest.support.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by Dempe on 2017/3/14.
 */
public class ForestNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("interceptors", new InterceptorBeanDefinitionParser());
        registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParser());
        registerBeanDefinitionParser("server", new ForestServerBeanDefinitionParser());
    }
}
