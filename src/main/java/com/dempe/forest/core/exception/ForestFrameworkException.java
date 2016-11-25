package com.dempe.forest.core.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public class ForestFrameworkException extends ForestAbstractException {
    private static final long serialVersionUID = -1638857395789735293L;

    public ForestFrameworkException() {
        super(MotanErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public ForestFrameworkException(MotanErrorMsg motanErrorMsg) {
        super(motanErrorMsg);
    }

    public ForestFrameworkException(String message) {
        super(message, MotanErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public ForestFrameworkException(String message, MotanErrorMsg motanErrorMsg) {
        super(message, motanErrorMsg);
    }

    public ForestFrameworkException(String message, Throwable cause) {
        super(message, cause, MotanErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public ForestFrameworkException(String message, Throwable cause, MotanErrorMsg motanErrorMsg) {
        super(message, cause, motanErrorMsg);
    }

    public ForestFrameworkException(Throwable cause) {
        super(cause, MotanErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR);
    }

    public ForestFrameworkException(Throwable cause, MotanErrorMsg motanErrorMsg) {
        super(cause, motanErrorMsg);
    }
}
