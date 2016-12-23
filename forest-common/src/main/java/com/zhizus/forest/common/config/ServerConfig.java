package com.zhizus.forest.common.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ServerConfig {

    @Value("${scan.base.package:com.zhizus.forest}")
    public String basePackage;

    // *********************system configuration*********************

    @Value("${tcp.noDelay:true}")
    public boolean tcpNoDelay;

    @Value("${tcp.keepAlive:true}")
    public boolean soKeepAlive;

    @Value("${tcp.backlog:65535}")
    public int soBacklog;


    // StandardThreadExecutor 业务线程池配置

    @Value("${biz.executor.core.thread:20}")
    public int coreThread;

    @Value("${biz.executor.max.thread:200}")
    public int maxThreads;

    //
    @Value("${http.port:8080}")
    public int httpPort;

    @Value("${http.server.start:false}")
    public boolean startHttpServer;

    @Value("${http.backlog:50}")
    public int httpBacklog;

    @Value("${zookeeper.connectString:localhost:2181}")
    public String zkConnectStr;

    @Value("${zookeeper.basePath:forest/service}")
    public String zkBasePath;




}
