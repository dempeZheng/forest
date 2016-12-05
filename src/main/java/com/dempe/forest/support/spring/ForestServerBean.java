package com.dempe.forest.support.spring;

import com.dempe.forest.AnnotationRouterMapping;
import com.dempe.forest.ForestExecutorGroup;
import com.dempe.forest.ServerConfig;
import com.dempe.forest.transport.NettyServer;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/5
 * Time: 11:54
 * To change this template use File | Settings | File Templates.
 */
public class ForestServerBean implements InitializingBean, DisposableBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private AnnotationRouterMapping mapping;

    private NettyServer nettyServer;

    private ForestExecutorGroup executorGroup;


    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (mapping == null) {
            mapping = new AnnotationRouterMapping(applicationContext);
        }
        ServerConfig config = ConfigFactory.create(ServerConfig.class);
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
