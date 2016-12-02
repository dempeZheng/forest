package com.dempe.forest.example;

import com.dempe.forest.core.AnnotationRouterMapping;
import com.dempe.forest.transport.NettyServer;
import com.dempe.forest.transport.ServerConfig;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/29
 * Time: 9:43
 * To change this template use File | Settings | File Templates.
 */
public class ServerMain {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application.xml"});
        new NettyServer(new AnnotationRouterMapping(context), ConfigFactory.create(ServerConfig.class)).doBind();
    }
}
