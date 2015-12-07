package com.dempe.ketty.ha.listener;

import java.util.EventObject;


public class HAEvent extends EventObject {

    private static final long serialVersionUID = 7282959056243872418L;

    private Object data;

    public HAEvent(Object source) {
        super(source);
    }

    public HAEvent(Object source, Object data) {
        super(source);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
