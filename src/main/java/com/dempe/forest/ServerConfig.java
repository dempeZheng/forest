package com.dempe.forest;

import org.aeonbits.owner.Config;

/**
 * Created by Dempe on 2016/12/7.
 */
@Config.Sources("classpath:server.properties")
public interface ServerConfig extends Config {

    // *********************system configuration*********************

    @Key("forest.port")
    @DefaultValue("9999")
    int port();

    @DefaultValue("true")
    boolean tcpNoDelay();

    @DefaultValue("true")
    boolean soKeepAlive();

    @DefaultValue("65535")
    int soBacklog();


    // StandardThreadExecutor 业务线程池配置

    @DefaultValue("20")
    int coreThread();

    @DefaultValue("200")
    int maxThreads();

    //
    @Key("http.port")
    @DefaultValue("8080")
    public int httpPort();

    @Key("http.backlog")
    @DefaultValue("50")
    int httpBacklog();

    @Key("zookeeper.connectString")
    @DefaultValue("")
    String zkConnectStr();

    @Key("zookeeper.basePath")
    @DefaultValue("forest")
    String zkBasePath();


}
