package com.dempe.forest.transport;

import org.aeonbits.owner.Config;

/**
 * 基于owner实现的配置文件类，功能强大
 * User: Dempe
 * Date: 2015/12/11
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
@Config.Sources("classpath:server.properties")
public interface ServerConfig extends Config {

    // *********************system configuration*********************

    @Key("server.port")
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


}
