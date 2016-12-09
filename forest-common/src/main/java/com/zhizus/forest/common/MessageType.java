package com.zhizus.forest.common;

import com.zhizus.forest.common.codec.Request;
import com.zhizus.forest.common.codec.Response;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/12/7 0007
 * Time: 下午 9:25
 * To change this template use File | Settings | File Templates.
 */
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

    public void setValue(byte value) {
        this.value = value;
    }

    public final static byte RESPONSE_MESSAGE_TYPE = (byte) (1 << 7);


}
