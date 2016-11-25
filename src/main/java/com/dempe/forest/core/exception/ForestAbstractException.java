package com.dempe.forest.core.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/25
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public abstract class ForestAbstractException extends RuntimeException {
    private static final long serialVersionUID = -8742311167276890503L;

    protected MotanErrorMsg motanErrorMsg = MotanErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR;
    protected String errorMsg = null;

    public ForestAbstractException() {
        super();
    }

    public ForestAbstractException(MotanErrorMsg motanErrorMsg) {
        super();
        this.motanErrorMsg = motanErrorMsg;
    }

    public ForestAbstractException(String message) {
        super(message);
        this.errorMsg = message;
    }

    public ForestAbstractException(String message, MotanErrorMsg motanErrorMsg) {
        super(message);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public ForestAbstractException(String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = message;
    }

    public ForestAbstractException(String message, Throwable cause, MotanErrorMsg motanErrorMsg) {
        super(message, cause);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public ForestAbstractException(Throwable cause) {
        super(cause);
    }

    public ForestAbstractException(Throwable cause, MotanErrorMsg motanErrorMsg) {
        super(cause);
        this.motanErrorMsg = motanErrorMsg;
    }

    @Override
    public String getMessage() {
        if (motanErrorMsg == null) {
            return super.getMessage();
        }

        String message;

        if (errorMsg != null && !"".equals(errorMsg)) {
            message = errorMsg;
        } else {
            message = motanErrorMsg.getMessage();
        }

        // TODO 统一上下文 requestid
        return "error_message: " + message + ", status: " + motanErrorMsg.getStatus() + ", error_code: " + motanErrorMsg.getErrorCode()
                + ",r=";
    }

    public int getStatus() {
        return motanErrorMsg != null ? motanErrorMsg.getStatus() : 0;
    }

    public int getErrorCode() {
        return motanErrorMsg != null ? motanErrorMsg.getErrorCode() : 0;
    }

    public MotanErrorMsg getMotanErrorMsg() {
        return motanErrorMsg;
    }
}
