package com.xcode.userservice.model.user.exception;

import java.time.LocalDate;

public class UserNotAllowedException extends DomainException{

    public UserNotAllowedException(String fecha) {
        super(DomainErrorCode.USER_NOT_ALLOWED, fecha);
    }
}
