package com.dempe.forest.codec;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class Message {

    private Header header;

    private byte[] payload;


    public Message() {
    }

    public Message(Header header, byte[] payload) {
        this.payload = payload;
        this.header = header;
    }

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

}
