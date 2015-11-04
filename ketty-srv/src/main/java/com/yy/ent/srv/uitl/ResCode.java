package com.yy.ent.srv.uitl;

/**
 * Ketty消息返回值定义枚举类
 * User: Dempe
 * Date: 2015/11/4
 * Time: 15:37
 * To change this template use File | Settings | File Templates.
 */
public enum ResCode {

    SUCCESS((short) 200, ""), NO_PERMISSION((short) 201, ResMsgBuilder.NO_PERMISSION_MSG);

    short resCode;

    String msg;

    private ResCode(short resCode, String msg) {
        this.resCode = resCode;
        this.msg = msg;
    }

    public short getResCode() {
        return resCode;
    }

    public void setResCode(short resCode) {
        this.resCode = resCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
