package com.dempe.ketty.srv.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/22
 * Time: 16:46
 * To change this template use File | Settings | File Templates.
 */
public class ModelConvertJsonException extends Exception {
    private static final long serialVersionUID = -9095702616213992991L;

    public ModelConvertJsonException() {
        super();
    }

    public ModelConvertJsonException(String message) {
        super(message);
    }

    public ModelConvertJsonException(Throwable cause) {
        super(cause);
    }

    public ModelConvertJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
