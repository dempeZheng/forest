package com.yy.ent.srv.interceptor;

import com.yy.ent.srv.core.ServerContext;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/3
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public interface KettyInterceptor {

    public boolean before(ServerContext context);

    public boolean after(ServerContext context, Object response);
}
