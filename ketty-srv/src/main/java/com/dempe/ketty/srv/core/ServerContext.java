package com.dempe.ketty.srv.core;

import com.codahale.metrics.MetricRegistry;
import com.dempe.ketty.srv.KettyServer;

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

    public MetricRegistry registry;

    public ServerContext(KettyServer.Builder builder, MetricRegistry registry) {
        this.builder = builder;
        this.mapping = new RequestMapping(builder);
        this.registry = registry;
    }

    public ActionMethod tackAction(String uri) {
        return mapping.tack(uri);
    }

}
