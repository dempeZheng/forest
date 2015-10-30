package com.yy.ent.protocol;

import com.yy.ent.pack.Pack;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/26
 * Time: 19:53
 * To change this template use File | Settings | File Templates.
 */
public class JettyResp implements Response {
    private long id;
    private String data;

    public JettyResp(long id, String data) {
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

    public byte[] encoder() throws UnsupportedEncodingException {
        Pack pack = new Pack();
        pack.putLong(id);
        pack.putVarstr(data);
        short length = (short) pack.size();
        byte[] bytes = new byte[length];
        pack.getBuffer().get(bytes);
        return bytes;
    }

    @Override
    public String toString() {
        return "JettyResp{" +
                "id=" + id +
                ", data='" + data + '\'' +
                '}';
    }
}
