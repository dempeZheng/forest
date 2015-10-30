package com.yy.ent.protocol;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.pack.Pack;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 19:53
 * To change this template use File | Settings | File Templates.
 */
public class GardenResp implements Response {
    private long id;
    private String data;

    public GardenResp(long id, String data) {
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

    public byte[] encoder() {
        Pack pack = new Pack();
        pack.putLong(id);
        return pack.getBuffer().array();
    }
}
