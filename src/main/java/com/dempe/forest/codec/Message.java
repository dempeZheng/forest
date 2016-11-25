package com.dempe.forest.codec;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class Message implements Codec{

    private Header header;

    private byte[] payload;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte[] encode(Object message) {
        return new byte[0];
    }

    public Object decode(byte[] buffer) {
        return null;
    }
}
