package com.yy.ent.protocol.json;


import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/20
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
public class Request {
    private long id;
    private String uri;
    private Map<String, String> params;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }


    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", uri='" + uri + '\'' +
                ", params=" + params +
                '}';
    }

    public String toJsonString() {
        return JSONObject.toJSON(this).toString();
    }


}
