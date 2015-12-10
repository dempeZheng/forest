package com.dempe.ketty.name;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/12/10
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public enum StatusCode {

    SUCCESS(0), EXIST_ERR(1), UN_EXIST_ERR(2);

    private int code;

    private StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
