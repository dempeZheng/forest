package com.yy.ent.srv.uitl;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/11/4
 * Time: 15:37
 * To change this template use File | Settings | File Templates.
 */
public enum ResCode {
    Success((short) 200), NO_PERMISSION((short) 201);
    short resCode;

    private ResCode(short resCode) {
        this.resCode = resCode;
    }

    public short getResCode() {
        return resCode;
    }

    public void setResCode(short resCode) {
        this.resCode = resCode;
    }
}
