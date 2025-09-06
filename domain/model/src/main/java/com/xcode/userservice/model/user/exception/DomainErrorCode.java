package com.xcode.userservice.model.user.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;


public enum DomainErrorCode {
    INVALID_EMAIL("USR_001", "The email isn't valid"),
    MISSING_REQUIRED_FIELD("USR_002", "The field is missing"),
    INVALID_SALARY("USR_003", "The salary is outside the allowed range."),
    USER_ALREADY_EXISTS("USR_004", "El usuario ya existe"),
    ROLE_NOT_FOUND("ROLE_001", "El rol no existe"),
    ROLE_NOT_ALLOWED("ROLE_002", "Rol no permitido para crear usuario");

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
