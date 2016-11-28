package com.dempe.forest.codec;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
public class Response {

    private short resCode;

    private String errMsg;

    public short getResCode() {
        return resCode;
    }

    public void setResCode(short resCode) {
        this.resCode = resCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
