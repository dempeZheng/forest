package com.dempe.forest.example;

import com.dempe.forest.core.interceptor.InvokerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

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
    public boolean before(Object target, Method method, Object... args) {
        LOGGER.debug("before args:{}", args);
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object result) {
        LOGGER.debug("after result:{}", result);
        return true;
    }
}
