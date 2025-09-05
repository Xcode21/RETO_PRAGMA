package com.xcode.userservice.model.user.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


public enum DomainErrorCode {
    INVALID_EMAIL("USR_001", "The email isn't valid"),
    MISSING_REQUIRED_FIELD("USR_002", "The field is missing"),
    INVALID_SALARY("USR_003", "The salary is outside the allowed range.");

    private final String code;
    private final String defaultMessage;

    DomainErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
