package com.yy.ent.protocol.json;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/20
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
public class Response {

    private long id;
    private String data;

    public Response(long id, String data) {
        this.id = id;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String toJsonStr() {
        return JSONObject.toJSONString(this);
    }
}
