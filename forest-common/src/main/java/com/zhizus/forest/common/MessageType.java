package com.zhizus.forest.common;

import com.zhizus.forest.common.codec.Request;
import com.zhizus.forest.common.codec.Response;


public enum MessageType {

    REQUEST((byte) 0), RESPONSE((byte) 1);

    private byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public static Class getMessageTypeByExtend(byte value) {
        switch (value & RESPONSE_MESSAGE_TYPE) {
            case 0x0:
                return Request.class;
            case RESPONSE_MESSAGE_TYPE:
                return Response.class;
            default:
                return Request.class;
        }

    }

    public byte getValue() {
        return value;
    }

    public final static byte RESPONSE_MESSAGE_TYPE = (byte) (1 << 7);


}
