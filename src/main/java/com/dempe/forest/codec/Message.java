package com.dempe.forest.codec;

/**
 * Created by Dempe on 2016/12/7.
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
