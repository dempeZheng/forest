package com.yy.ent.srv.interceptor;

import com.yy.ent.srv.core.ServerContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/3
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public class BaseInterceptor implements KettyInterceptor {


    @Override
    public boolean before(ServerContext context) {
        return true;
    }

    @Override
    public boolean after(ServerContext context, Object response) {
        return true;
    }
}
