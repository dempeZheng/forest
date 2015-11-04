package com.yy.ent.srv.interceptor;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/3
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public class BaseInterceptor implements KettyInterceptor {


    @Override
    public boolean before() {
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }
}
