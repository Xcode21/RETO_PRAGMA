package com.xcode.userservice.api.exception;

import com.xcode.userservice.model.user.exception.DomainException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xcode.userservice.api.exception.ApiErrorCode.VALIDATION_ERROR;

@Component
@Slf4j
public class GlobalErrorAttribute extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorMap = new HashMap<>();
        Throwable error = this.getError(request);

        CustomErrorResponse errorInfo = determineErrorInfo(error);
        putMessageLog(error, errorInfo, request);

        errorMap.put("code", errorInfo.getCode());
        errorMap.put("message", errorInfo.getMessage());
        if (errorInfo.getData() != null) {
            errorMap.put("data", errorInfo.getData());
        }
        errorMap.put("path", request.path());

        return errorMap;
    }

    private void putMessageLog(Throwable error, CustomErrorResponse errorInfo, ServerRequest request) {
        String method = request.method().name();
        String path = request.path();
        switch (errorInfo.getLogLevel()) {
            case "info" -> {
                log.info("Client request: {} {} → {} ({})",
                        method, path, errorInfo.getStatusCode(), error.getMessage());
            }
            case "warn" -> {
                log.warn("Client error: {} {} → {} - {}",
                        method, path, errorInfo.getStatusCode(), error.getMessage());
            }
            case "error" -> {
                log.error("Server error: {} {} → {} - [{}]",
                        method, path, errorInfo.getStatusCode(), error.getMessage(), error);
            }
        }

    }

    private CustomErrorResponse determineErrorInfo(Throwable error) {

        if (error instanceof DomainException domainEx) {
            HttpStatus httpStatus = mapDomainErrorToHttpStatus(domainEx.getErrorCode());
            return CustomErrorResponse.builder()
                    .code(domainEx.getErrorCode())
                    .message(domainEx.getErrorMessage())
                    .statusCode(httpStatus.value())
                    .logLevel("warn")
                    .build();
        }

        if (error instanceof ConstraintViolationException ex) {
            List<ValidationFieldError> fieldErrors = ex.getConstraintViolations()
                    .stream()
                    .map(violation -> ValidationFieldError
                            .builder()
                            .field(getFieldName(violation.getPropertyPath()))
                            .message(violation.getMessage())
                            .rejectedValue(violation.getInvalidValue())
                            .build())
                    .collect(Collectors.toList());

            ValidationErrorData validationData = ValidationErrorData.builder()
                    .fieldErrors(fieldErrors)
                    .totalErrors(fieldErrors.size())
                    .build();

            return CustomErrorResponse.builder()
                    .code(VALIDATION_ERROR.getCode())
                    .message(VALIDATION_ERROR.getDefaultMessage())
                    .data(validationData)
                    .statusCode(VALIDATION_ERROR.getHttpStatus().value())
                    .logLevel("warn")
                    .build();
        }

        if (error instanceof WebExchangeBindException ex) {
            List<ValidationFieldError> fieldErrors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(fieldError -> ValidationFieldError
                            .builder()
                            .field(fieldError.getField())
                            .message(fieldError.getDefaultMessage())
                            .rejectedValue(fieldError.getRejectedValue())
                            .build())
                    .collect(Collectors.toList());

            ValidationErrorData validationData = ValidationErrorData.builder()
                    .fieldErrors(fieldErrors)
                    .totalErrors(fieldErrors.size())
                    .build();

            return CustomErrorResponse.builder()
                    .code(VALIDATION_ERROR.getCode())
                    .message(VALIDATION_ERROR.getDefaultMessage())
                    .data(validationData)
                    .statusCode(VALIDATION_ERROR.getHttpStatus().value())
                    .logLevel("warn")
                    .build();
        }

        if (error instanceof ServerWebInputException) {
            return CustomErrorResponse.builder()
                    .code(ApiErrorCode.REQUEST_BODY_REQUIRED.getCode())
                    .message(ApiErrorCode.REQUEST_BODY_REQUIRED.getDefaultMessage())
                    .statusCode(ApiErrorCode.REQUEST_BODY_REQUIRED.getHttpStatus().value())
                    .logLevel("warn")
                    .build();
        }

        return analyzeErrorCauseOrDefault(error);
    }

    private HttpStatus mapDomainErrorToHttpStatus(String domainErrorCode) {
        return switch (domainErrorCode) {
            case "USR_001", "USR_002", "USR_003" -> HttpStatus.BAD_REQUEST;
            case "USR_004" -> HttpStatus.CONFLICT;
            case "USR_005", "ROLE_001" -> HttpStatus.NOT_FOUND;
            case "ROLE_002" -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.BAD_REQUEST;
        };
    }

    private CustomErrorResponse analyzeErrorCauseOrDefault(Throwable error) {
        Throwable rootCause = getRootCause(error);
        String errorMessage = error.getMessage();
        String rootMessage = rootCause.getMessage();

        if (rootCause instanceof ClassCastException ||
                rootMessage != null && rootMessage.contains("mapping")) {
            log.error("Mapping error detected: {}", rootMessage, rootCause);
            return CustomErrorResponse.builder()
                    .code(ApiErrorCode.MAPPING_ERROR.getCode())
                    .message(ApiErrorCode.MAPPING_ERROR.getDefaultMessage())
                    .statusCode(ApiErrorCode.MAPPING_ERROR.getHttpStatus().value())
                    .logLevel("error")
                    .build();
        }

        if (rootMessage != null && (
                rootMessage.contains("database") ||
                        rootMessage.contains("connection") ||
                        rootMessage.contains("timeout"))) {
            log.error("Database error detected: {}", rootMessage, rootCause);
            return CustomErrorResponse.builder()
                    .code(ApiErrorCode.DATABASE_CONNECTION_ERROR.getCode())
                    .message(ApiErrorCode.DATABASE_CONNECTION_ERROR.getDefaultMessage())
                    .statusCode(ApiErrorCode.DATABASE_CONNECTION_ERROR.getHttpStatus().value())
                    .logLevel("error")
                    .build();
        }

        if (rootMessage != null && (
                rootMessage.contains("duplicate") ||
                        rootMessage.contains("unique") ||
                        rootMessage.contains("constraint"))) {
            log.warn("Data integrity error detected: {}", rootMessage);
            return CustomErrorResponse.builder()
                    .code(ApiErrorCode.DATA_INTEGRITY_ERROR.getCode())
                    .message(ApiErrorCode.DATA_INTEGRITY_ERROR.getDefaultMessage())
                    .statusCode(ApiErrorCode.DATA_INTEGRITY_ERROR.getHttpStatus().value())
                    .logLevel("warn")
                    .build();
        }

        log.error("Unhandled error: {}", error.getMessage(), error);
        return CustomErrorResponse.builder()
                .code(ApiErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message(ApiErrorCode.INTERNAL_SERVER_ERROR.getDefaultMessage())
                .statusCode(ApiErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value())
                .logLevel("error")
                .build();
    }

    private Throwable getRootCause(Throwable error) {
        Throwable cause = error;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }

    private String getFieldName(Path propertyPath) {
        String fieldName = "";
        for (Path.Node node : propertyPath) {
            fieldName = node.getName();
        }
        return fieldName;
    }
}
