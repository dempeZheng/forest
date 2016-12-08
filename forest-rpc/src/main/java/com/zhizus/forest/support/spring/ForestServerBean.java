package com.zhizus.forest.support.spring;

import com.zhizus.forest.AnnotationRouterMapping;
import com.zhizus.forest.ForestExecutorGroup;
import com.zhizus.forest.ServerConfig;
import com.zhizus.forest.transport.NettyServer;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ForestServerBean implements InitializingBean, DisposableBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private AnnotationRouterMapping mapping;

    private NettyServer nettyServer;

    private ForestExecutorGroup executorGroup;


    private ServerConfig config;

    @Override
    public void destroy() throws Exception {
        if (nettyServer != null) {
            nettyServer.close();
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (mapping == null) {
            mapping = new AnnotationRouterMapping(applicationContext);
        }
        this.config = ConfigFactory.create(ServerConfig.class);
        if (executorGroup == null) {
            executorGroup = new ForestExecutorGroup(config, mapping.listGroup(), applicationContext);
        }
        nettyServer = new NettyServer(mapping, config, executorGroup);
        nettyServer.doBind();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setMapping(AnnotationRouterMapping mapping) {
        this.mapping = mapping;
    }


    public void setExecutorGroup(ForestExecutorGroup executorGroup) {
        this.executorGroup = executorGroup;
    }

}
