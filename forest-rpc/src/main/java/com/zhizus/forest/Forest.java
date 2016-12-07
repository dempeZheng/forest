package com.zhizus.forest;

import com.zhizus.forest.client.proxy.ForestDynamicProxy;
import com.zhizus.forest.common.config.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dempe on 2016/12/7.
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
