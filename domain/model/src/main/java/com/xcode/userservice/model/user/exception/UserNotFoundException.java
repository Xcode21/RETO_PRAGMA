package com.xcode.userservice.model.user.exception;

import java.util.UUID;

public class UserNotFoundException extends DomainException{
    public UserNotFoundException(UUID userId) {
        super(DomainErrorCode.USER_NOT_FOUND, "User not found with ID: " + userId);
    }

    public UserNotFoundException(String message) {
        super(DomainErrorCode.USER_NOT_FOUND, message);
    }
}
