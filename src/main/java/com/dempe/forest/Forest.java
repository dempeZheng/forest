package com.dempe.forest;

import com.dempe.forest.client.proxy.ForestDynamicProxy;
import com.dempe.forest.config.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/6 0006
 * Time: 下午 9:06
 * To change this template use File | Settings | File Templates.
 */
public class Forest {

    private final static Logger LOGGER = LoggerFactory.getLogger(Forest.class);

    public static <T> T from(Class<T> clazz) throws InterruptedException {
        return ForestDynamicProxy.newInstance(clazz);
    }

    public static <T> T from(Class<T> clazz, ServiceConfig config) throws InterruptedException {

        return ForestDynamicProxy.newInstance(clazz, config);
    }

    public static void createServer() {

    }
}
