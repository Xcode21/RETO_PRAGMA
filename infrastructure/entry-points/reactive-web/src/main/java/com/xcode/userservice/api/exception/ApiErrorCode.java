package com.xcode.userservice.api.exception;

import org.springframework.http.HttpStatus;

public enum ApiErrorCode {
    VALIDATION_ERROR("API_001", "Erro validation data", HttpStatus.BAD_REQUEST),
    REQUEST_BODY_REQUIRED("API_002", "Requests body is required", HttpStatus.BAD_REQUEST),
    MAPPING_ERROR("API_003", "Error in data processing", HttpStatus.UNPROCESSABLE_ENTITY),
    DATABASE_CONNECTION_ERROR("API_004", "Database connection error", HttpStatus.SERVICE_UNAVAILABLE),
    DATA_INTEGRITY_ERROR("API_005", "The resource already exists or violates a restriction", HttpStatus.CONFLICT),
    METHOD_NOT_ALLOWED("API_006", "HTTP method not allowed", HttpStatus.METHOD_NOT_ALLOWED),
    UNSUPPORTED_MEDIA_TYPE("API_007", "Unsupported content type", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    NOT_ACCEPTABLE("API_008", "Unacceptable response format", HttpStatus.NOT_ACCEPTABLE),
    RESOURCE_NOT_FOUND("API_404", "Resource not found", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("API_999", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus httpStatus;

    ApiErrorCode(String code, String defaultMessage, HttpStatus httpStatus) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}