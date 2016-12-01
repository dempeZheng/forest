package com.dempe.forest.example;

import com.dempe.forest.core.interceptor.InvokerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/1
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PrintInterceptor implements InvokerInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(PrintInterceptor.class);

    @Override
    public boolean before(Object... args) {
        LOGGER.info("before args:{}", args);
        return true;
    }

    @Override
    public boolean after(Object result) {
        LOGGER.info("after result:{}", result);
        return true;
    }
}
