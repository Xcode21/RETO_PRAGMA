package com.xcode.userservice.model.user.exception;

public enum DomainErrorCode {
    INVALID_EMAIL("USR_001", "The email isn't valid"),
    MISSING_REQUIRED_FIELD("USR_002", "The field is missing"),
    INVALID_SALARY("USR_003", "The salary is outside the allowed range."),
    USER_ALREADY_EXISTS("USR_004", "The user already exists"),
    USER_NOT_ALLOWED("USR_005", "The user must be over 18 years old"),
    ROLE_NOT_FOUND("ROLE_001", "The role dont exists"),
    ROLE_NOT_ALLOWED("ROLE_002", "Role not allowed for user creation"),
    USER_NOT_FOUND("USR_005", "User not found");
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
