package com.dempe.forest.codec;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
public class Response  implements Serializable{

    private short resCode = 0;

    private String errMsg = "";

    private Object object;


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

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

    @Override
    public String toString() {
        return "Response{" +
                "resCode=" + resCode +
                ", errMsg='" + errMsg + '\'' +
                ", object=" + object +
                '}';
    }
}
