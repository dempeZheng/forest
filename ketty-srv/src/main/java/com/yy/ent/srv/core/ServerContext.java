package com.yy.ent.srv.core;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:45
 * To change this template use File | Settings | File Templates.
 */
public class ServerContext {

    private RequestMapping mapping = new RequestMapping();

    public ActionMethod get(String uri) {
        return mapping.tack(uri);
    }


}
