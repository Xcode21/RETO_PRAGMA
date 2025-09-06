package com.xcode.userservice.r2dbc.exception;

public class InfrastructureException extends RuntimeException {
    private final InfraErrorCode errorCode;

    public InfrastructureException(InfraErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public InfraErrorCode getErrorCode() {
        return errorCode;
    }
}
