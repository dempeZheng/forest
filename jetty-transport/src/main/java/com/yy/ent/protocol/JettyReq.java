package com.yy.ent.protocol;

import com.alibaba.fastjson.JSONObject;
import com.yy.ent.pack.Pack;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
public class JettyReq implements Request {


    private long id;

    private Header header;

    public long getId() {
        return id;
    }

    public String getUri() {
        return header.getUri();
    }

    public JettyReq() {
        header = new Header();
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setUri(String uri) {
        header.setUri(uri);
    }

    public void setParameter(JSONObject param) {
        header.setParam(param);
    }

    public JSONObject getParameter() {
        return header.getParam();
    }


    public byte[] encoder() throws UnsupportedEncodingException {
        Pack pack = new Pack();
        pack.putLong(id);
        pack.putVarstr(getUri());
        pack.putVarstr(getParameter().toJSONString());
        short length = (short) pack.size();
        byte[] bytes = new byte[length];
        pack.getBuffer().get(bytes);
        return bytes;
    }

    @Override
    public String toString() {
        return "GardenReq{" +
                "id=" + id +
                ", header=" + header +
                '}';
    }
}
