package com.yy.ent.protocol;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
public class GardenReq implements Request {


    private long id;

    private Header header;

    private String uri;


    public long getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public void setId(long id) {

    }

    @Override
    public void setUri(String uri) {

    }

    public JSONObject getParameter() {
        return header.getParam();
    }
}
