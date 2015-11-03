package com.yy.ent.halb.exception;

public class HAClientChangeException extends Exception {

    private static final long serialVersionUID = 1L;


    public HAClientChangeException() {
        super();
    }


    public HAClientChangeException(String message) {
        super(message);
    }


    public HAClientChangeException(String message, Throwable cause) {
        super(message, cause);
    }


    public HAClientChangeException(Throwable cause) {
        super(cause);
    }
}
