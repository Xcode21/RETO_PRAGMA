package com.xcode.userservice.model.user.exception;


public class DomainException extends RuntimeException {

    private final DomainErrorCode errorCode;

    public DomainException(DomainErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public DomainException(DomainErrorCode errorCode, String detail) {
        super(errorCode.getDefaultMessage() + ": " + detail);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode.getCode();
    }

    public String getErrorMessage() {
        return super.getMessage();
    }
}
