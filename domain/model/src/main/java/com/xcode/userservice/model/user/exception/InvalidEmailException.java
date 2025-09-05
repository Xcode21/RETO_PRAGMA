package com.xcode.userservice.model.user.exception;

public class InvalidEmailException extends DomainException {

    public InvalidEmailException(String email) {
        super(DomainErrorCode.INVALID_EMAIL, email);
    }
}
