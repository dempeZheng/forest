package com.yy.ent.srv.core;

import com.yy.ent.protocol.Response;
import com.yy.ent.protocol.json.Request;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:21
 * To change this template use File | Settings | File Templates.
 */
public interface Take {

    Response act();

    Take route(Request request);

}
