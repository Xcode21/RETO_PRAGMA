package com.xcode.userservice.model.user.exception;

public enum DomainErrorCode {
    INVALID_EMAIL("USR_001", "The email isn't valid", 400),
    MISSING_REQUIRED_FIELD("USR_002", "The field is missing", 400),
    INVALID_SALARY("USR_003", "The salary is outside the allowed range.", 400),
    USER_ALREADY_EXISTS("USR_004", "The user already exists", 409),
    USER_NOT_ALLOWED("USR_005", "The user must be over 18 years old", 400),
    USER_NOT_FOUND("USR_006", "User not found", 404),
    ROLE_NOT_FOUND("ROLE_001", "The role dont exists", 404),
    ROLE_NOT_ALLOWED("ROLE_002", "Role not allowed for user creation", 403);
    
    private final String code;
    private final String defaultMessage;
    private final int httpStatusCode;

    DomainErrorCode(String code, String defaultMessage, int httpStatusCode) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.httpStatusCode = httpStatusCode;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
    
    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
