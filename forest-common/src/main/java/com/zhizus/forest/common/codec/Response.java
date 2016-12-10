package com.zhizus.forest.common.codec;


import com.zhizus.forest.common.exception.ForestErrorMsg;

import java.io.Serializable;

/**
 * Created by Dempe on 2016/12/7.
 */
public class Response implements Serializable {

    private int code = 0;

    private String errMsg = "";

    private Object result;


    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public void setForestErrorMsg(ForestErrorMsg forestErrorMsg) {
        this.errMsg = forestErrorMsg.getMessage();
        this.code = forestErrorMsg.getErrorCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", errMsg='" + errMsg + '\'' +
                ", result=" + result +
                '}';
    }
}
