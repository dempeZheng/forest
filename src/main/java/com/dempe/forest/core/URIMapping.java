package com.dempe.forest.core;

import com.dempe.forest.core.invoker.InvokerWrapper;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/28
 * Time: 9:59
 * To change this template use File | Settings | File Templates.
 */
public class URIMapping  implements ApplicationListener<ContextRefreshedEvent> {

    @Value("base.package")
    private String basePackage;

    private Map<String, InvokerWrapper> mapping = Maps.newConcurrentMap();

    public void initMapping() {
        // init mapping

    }

    public InvokerWrapper getInvokerWrapperByURI(String uri) {
        return mapping.get(uri);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

       // spring 容器初始化完成后调用
         initMapping();
    }

}
