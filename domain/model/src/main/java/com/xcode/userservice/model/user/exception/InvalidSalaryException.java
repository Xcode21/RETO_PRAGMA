package com.xcode.userservice.model.user.exception;

public class InvalidSalaryException extends DomainException{

    public InvalidSalaryException(Double salary) {
        super(DomainErrorCode.INVALID_SALARY, String.valueOf(salary));
    }
}
