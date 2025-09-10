package com.xcode.userservice.model.user.exception;

public class InvalidCredentialException extends DomainException {

    public InvalidCredentialException(String email) {
        super(DomainErrorCode.INVALID_CREDENTIALS, email);
    }
}
