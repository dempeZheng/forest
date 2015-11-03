package com.yy.ent.intercetptor;


import com.yy.ent.mvc.anno.Around;
import com.yy.ent.srv.core.ServerContext;
import com.yy.ent.srv.interceptor.BaseInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/3
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
@Around
public class EchoInterceptor extends BaseInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(EchoInterceptor.class);


    @Override
    public boolean before(ServerContext context) {
        LOGGER.info("==============EchoInterceptor before=========context:{}", context);
        return true;
    }

    @Override
    public boolean after(ServerContext context, Object response) {
        LOGGER.info("==============EchoInterceptor after=========context:{},response:{}", context, response);
        return true;
    }
}
