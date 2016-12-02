package com.dempe.forest.example;

import com.dempe.forest.AnnotationRouterMapping;
import com.dempe.forest.transport.HttpForestServer;
import com.dempe.forest.transport.NettyServer;
import com.dempe.forest.ServerConfig;
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
        AnnotationRouterMapping mapping = new AnnotationRouterMapping(context);
        ServerConfig config = ConfigFactory.create(ServerConfig.class);
        /**
         * 对于同一业务接口，可以同时暴露两种协议
         */
        // 启动rpc服务
        new NettyServer(mapping, config).doBind();
        // 启动http服务
        new HttpForestServer(mapping, config).start();

    }
}
