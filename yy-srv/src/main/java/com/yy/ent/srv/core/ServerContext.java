package com.yy.ent.srv.core;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.srv.method.ActionMethod;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:45
 * To change this template use File | Settings | File Templates.
 */
public class ServerContext {

    private RequestMapping mapping = new RequestMapping();


    private ThreadLocal<JSONObject> reqContext = new ThreadLocal<JSONObject>();

    private JSONObject params;


    public void setRequestContext(JSONObject params) {
        reqContext.set(params);
    }

    public JSONObject getRequestParams() {
        return reqContext.get();
    }


    public ServerContext() {

    }

    public ActionMethod get(String uri) {
        return mapping.tack(uri);
    }


}
