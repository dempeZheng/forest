package com.dempe.forest;

import com.dempe.forest.transport.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class ForestServer {

    private ServerBuilder builder;
    private NettyServer server;

    public ForestServer(ServerBuilder builder) {
        this.builder = builder;
    }

    public void start() {
        builder.xmlInitSpring("");


    }


}

class ServerBuilder {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerBuilder.class);
    private String host;
    private int port;
    private String basePackage;
    private String configPath;
    private ApplicationContext context;

    private volatile boolean springInitStat;

    protected synchronized ServerBuilder annotationInitSpring(Class clazz) {
        if (springInitStat) {
            LOGGER.info("spring context is already init");
            return this;
        }
        context = new AnnotationConfigApplicationContext(clazz);
        LOGGER.info("spring context init by annotation");
        springInitStat = true;
        return this;
    }

    protected synchronized ServerBuilder xmlInitSpring(String configPath) {
        if (springInitStat) {
            LOGGER.info("spring context is already init");
            return this;
        }
        context = new ClassPathXmlApplicationContext(new String[]{configPath});
        LOGGER.info("spring context init by xml");
        springInitStat = true;
        return this;
    }

    public ServerBuilder host(String host) {
        this.host = host;
        return this;
    }

    public ServerBuilder port(int port) {
        this.port = port;
        return this;
    }

    public ForestServer build() {
        return new ForestServer(this);
    }


}
