package com.dempe.forest.core;

import com.dempe.forest.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public enum MessageType {

    request((byte) 0), response(Constants.RSP_TYPE);

    private byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    static MessageType getMessageTypeByValue(byte value) {
        switch (value) {
            case 0:
                return request;
            case  (Constants.RSP_TYPE):
                return response;
            default:
                return request;
        }
    }

}
