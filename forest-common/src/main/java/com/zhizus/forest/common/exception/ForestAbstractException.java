package com.zhizus.forest.common.exception;

/**
 * Created by Dempe on 2016/12/7.
 */
public abstract class ForestAbstractException extends RuntimeException {
    private static final long serialVersionUID = -8742311167276890503L;

    protected ForestErrorMsg forestErrorMsg = ForestErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR;
    protected String errorMsg = null;

    public ForestAbstractException() {
        super();
    }

    public ForestAbstractException(ForestErrorMsg forestErrorMsg) {
        super();
        this.forestErrorMsg = forestErrorMsg;
    }

    public ForestAbstractException(String message) {
        super(message);
        this.errorMsg = message;
    }

    public ForestAbstractException(String message, ForestErrorMsg forestErrorMsg) {
        super(message);
        this.forestErrorMsg = forestErrorMsg;
        this.errorMsg = message;
    }

    public ForestAbstractException(String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = message;
    }

    public ForestAbstractException(String message, Throwable cause, ForestErrorMsg forestErrorMsg) {
        super(message, cause);
        this.forestErrorMsg = forestErrorMsg;
        this.errorMsg = message;
    }

    public ForestAbstractException(Throwable cause) {
        super(cause);
    }

    public ForestAbstractException(Throwable cause, ForestErrorMsg forestErrorMsg) {
        super(cause);
        this.forestErrorMsg = forestErrorMsg;
    }

    @Override
    public String getMessage() {
        if (forestErrorMsg == null) {
            return super.getMessage();
        }

        String message;

        if (errorMsg != null && !"".equals(errorMsg)) {
            message = errorMsg;
        } else {
            message = forestErrorMsg.getMessage();
        }

        // TODO 统一上下文 requestid
        return "error_message: " + message + ", status: " + forestErrorMsg.getStatus() + ", error_code: " + forestErrorMsg.getErrorCode()
                + ",r=";
    }

    public int getStatus() {
        return forestErrorMsg != null ? forestErrorMsg.getStatus() : 0;
    }

    public int getErrorCode() {
        return forestErrorMsg != null ? forestErrorMsg.getErrorCode() : 0;
    }

    public ForestErrorMsg getForestErrorMsg() {
        return forestErrorMsg;
    }
}
