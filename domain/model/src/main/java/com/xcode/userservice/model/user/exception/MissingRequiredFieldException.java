package com.xcode.userservice.model.user.exception;

public class MissingRequiredFieldException extends DomainException {
    public MissingRequiredFieldException(String field) {
        super(DomainErrorCode.MISSING_REQUIRED_FIELD, field);
    }
}
