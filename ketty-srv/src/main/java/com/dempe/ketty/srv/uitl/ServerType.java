package com.dempe.ketty.srv.uitl;

/**
 * 服务类型
 * 1==>httpServer
 * 2==>KettyServer(自定义私有协议)
 * User: Dempe
 * Date: 2015/11/3
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public enum ServerType {

    HTTP_SERVER(1), KETTY_SERVER(2);
    int value;

    private ServerType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
