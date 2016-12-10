package com.zhizus.forest;

import org.aeonbits.owner.Config;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/2
 * Time: 15:49
 * To change this template use File | Settings | File Templates.
 */
@Config.Sources("classpath:client.properties")
public interface ClientConfig extends Config {

    @DefaultValue("localhost")
    String host();

    @DefaultValue("9999")
    int port();
}
