package com.yy.ent.protocol;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhengdaxia
 * Date: 15/10/17
 * Time: 上午10:25
 * To change this template use File | Settings | File Templates.
 */
public class Header {

    /**
     * 消息唯一id，用于异步消息返回信息的标识
     */
    private Long id;

    private String uri;

    private JSONObject param;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JSONObject getParam() {
        return param;
    }

    public void setParam(JSONObject param) {
        this.param = param;
    }
}
