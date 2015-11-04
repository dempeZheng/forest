package com.yy.ent.srv.core;

import com.yy.ent.srv.Conf;
import com.yy.ent.srv.KettyServer;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:45
 * To change this template use File | Settings | File Templates.
 */
public abstract class ServerContext {

    public RequestMapping mapping;

    public KettyServer.Builder builder;

    public ServerContext(KettyServer.Builder builder) {
        this.builder = builder;
        this.mapping = new RequestMapping(builder);
    }

    public ActionMethod tackAction(String uri) {
        return mapping.tack(uri);
    }


}
