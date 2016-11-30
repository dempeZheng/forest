package com.dempe.forest.core;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public enum InvokeType {

    normal((byte) 0), oneway((byte) (1 << 3)), async((byte) (2 << 3));

    private byte value;


    InvokeType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    static InvokeType getInvokeTypeByValue(byte value) {
        switch (value) {
            case 0x0:
                return normal;
            case 1 << 3:
                return oneway;
            case 2 << 3:
                return async;
            default:
                return normal;
        }
    }

}
