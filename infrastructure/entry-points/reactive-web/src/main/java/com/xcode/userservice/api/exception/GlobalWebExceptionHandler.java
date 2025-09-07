package com.xcode.userservice.api.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.xcode.userservice.model.user.exception.DomainErrorCode;
import com.xcode.userservice.model.user.exception.DomainException;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.codec.CodecException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.*;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.xcode.userservice.api.exception.ApiErrorCode.*;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class GlobalWebExceptionHandler implements WebExceptionHandler {
    private final ObjectMapper objectMapper;
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getPath().value();

        CustomErrorResponse errorResponse = buildErrorResponse(ex, method, path);

        if (errorResponse != null) {
            logException(method, path, errorResponse, ex);
            return buildResponse(exchange, errorResponse,
                    HttpStatus.valueOf(errorResponse.getStatusCode()));
        }

        return Mono.error(ex);
    }

    private CustomErrorResponse buildErrorResponse(Throwable ex, String method, String path) {

        if (ex instanceof DomainException domainEx) {
            HttpStatus httpStatus = mapDomainErrorToHttpStatus(domainEx.getErrorCode());
            return CustomErrorResponse.builder()
                    .code(domainEx.getErrorCode())
                    .message(domainEx.getErrorMessage())
                    .data(null)
                    .path(path)
                    .statusCode(httpStatus.value())
                    .logLevel("warn")
                    .build();
        }


        if (ex instanceof WebExchangeBindException bindEx) {
            return buildValidationErrorResponse(bindEx.getBindingResult().getFieldErrors()
                    .stream()
                    .map(fieldError -> ValidationFieldError.builder()
                            .field(fieldError.getField())
                            .message(fieldError.getDefaultMessage())
                            //.rejectedValue(fieldError.getRejectedValue())
                            .build())
                    .collect(Collectors.toList()), path);
        }

        if (ex instanceof ConstraintViolationException cvEx) {
            return buildValidationErrorResponse(cvEx.getConstraintViolations()
                    .stream()
                    .map(violation -> ValidationFieldError.builder()
                            .field(extractFieldName(violation.getPropertyPath()))
                            .message(violation.getMessage())
                            // .rejectedValue(violation.getInvalidValue())
                            .build())
                    .collect(Collectors.toList()), path);
        }

        if (ex instanceof ServerWebInputException) {
            return handleServerWebInputException(ex, path);
        }

        if (ex instanceof CodecException) {
            return CustomErrorResponse.builder()
                    .code(REQUEST_BODY_REQUIRED.getCode())
                    .message(REQUEST_BODY_REQUIRED.getDefaultMessage())
                    .path(path)
                    .statusCode(REQUEST_BODY_REQUIRED.getHttpStatus().value())
                    .logLevel("warn")
                    .build();
        }

        if (ex instanceof MethodNotAllowedException) {
            return CustomErrorResponse.builder()
                    .code(METHOD_NOT_ALLOWED.getCode())
                    .message(METHOD_NOT_ALLOWED.getDefaultMessage())
                    .path(path)
                    .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                    .logLevel("warn")
                    .build();
        }

        if (ex instanceof UnsupportedMediaTypeStatusException) {
            return CustomErrorResponse.builder()
                    .code(UNSUPPORTED_MEDIA_TYPE.getCode())
                    .message(UNSUPPORTED_MEDIA_TYPE.getDefaultMessage())
                    .path(path)
                    .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                    .logLevel("warn")
                    .build();
        }

        if (ex instanceof ResponseStatusException statusEx) {
            return CustomErrorResponse.builder()
                    .code(INTERNAL_SERVER_ERROR.getCode())
                    .message(statusEx.getReason() != null ? statusEx.getReason() : INTERNAL_SERVER_ERROR.getDefaultMessage())
                    .path(path)
                    .statusCode(statusEx.getStatusCode().value())
                    .logLevel(statusEx.getStatusCode().is5xxServerError() ? "error" : "warn")
                    .build();
        }

        return analyzeGenericException(ex, path);
    }

    private CustomErrorResponse buildValidationErrorResponse(List<ValidationFieldError> fieldErrors, String path) {
        ValidationErrorData validationData = ValidationErrorData.builder()
                .fieldErrors(fieldErrors)
                .totalErrors(fieldErrors.size())
                .build();

        return CustomErrorResponse.builder()
                .code(VALIDATION_ERROR.getCode())
                .message(VALIDATION_ERROR.getDefaultMessage())
                .data(validationData)
                .path(path)
                .statusCode(VALIDATION_ERROR.getHttpStatus().value())
                .logLevel("warn")
                .build();
    }

    private CustomErrorResponse handleServerWebInputException(Throwable ex, String path) {
        Throwable rootCause = getRootCause(ex);

        if (rootCause instanceof DateTimeParseException dateEx) {
            String errorMessage = "Formato de fecha inválido: " + dateEx.getMessage();

            String fieldName = findFieldNameInExceptionChain(ex, "dateField");
            
            List<ValidationFieldError> fieldErrors = List.of(
                    ValidationFieldError.builder()
                            .field(fieldName)
                            .message(errorMessage)
                            .build()
            );

            return buildValidationErrorResponse(fieldErrors, path);
        }
        
        if (rootCause instanceof InvalidFormatException formatEx) {
            String fieldPath = extractFieldPath(formatEx.getPath());
            String errorMessage = "Invalid format for the field: " + fieldPath;

            List<ValidationFieldError> fieldErrors = List.of(
                    ValidationFieldError.builder()
                            .field(fieldPath)
                            .message(errorMessage)
                            .build()
            );

            return buildValidationErrorResponse(fieldErrors, path);
        }

        if (rootCause instanceof JsonMappingException jsonEx) {
            String fieldPath = extractFieldPath(jsonEx.getPath());
            String errorMessage = "Error in the field '" + fieldPath + "': " + jsonEx.getOriginalMessage();

            List<ValidationFieldError> fieldErrors = List.of(
                    ValidationFieldError.builder()
                            .field(fieldPath)
                            .message(errorMessage)
                            .build()
            );

            return buildValidationErrorResponse(fieldErrors, path);
        }

        if (rootCause instanceof JsonProcessingException jsonProcessingEx) {

            return CustomErrorResponse.builder()
                    .code(FORMAT_JSON_NOT_ACCEPTABLE.getCode())
                    .message(FORMAT_JSON_NOT_ACCEPTABLE.getDefaultMessage())
                    .path(path)
                    .statusCode(FORMAT_JSON_NOT_ACCEPTABLE.getHttpStatus().value())
                    .logLevel("warn")
                    .build();
        }

        return CustomErrorResponse.builder()
                .code(REQUEST_BODY_REQUIRED.getCode())
                .message(REQUEST_BODY_REQUIRED.getDefaultMessage() + ": " + ex.getMessage())
                .path(path)
                .statusCode(REQUEST_BODY_REQUIRED.getHttpStatus().value())
                .logLevel("warn")
                .build();
    }

    private CustomErrorResponse analyzeGenericException(Throwable ex, String path) {
        Throwable rootCause = getRootCause(ex);
        String rootMessage = rootCause.getMessage();

        if (rootMessage != null) {
            if (rootMessage.contains("duplicate") || rootMessage.contains("unique") ||
                    rootMessage.contains("constraint")) {
                return CustomErrorResponse.builder()
                        .code(DATA_INTEGRITY_ERROR.getCode())
                        .message(DATA_INTEGRITY_ERROR.getDefaultMessage())
                        .path(path)
                        .statusCode(DATA_INTEGRITY_ERROR.getHttpStatus().value())
                        .logLevel("warn")
                        .build();
            }

            if (rootMessage.contains("database") || rootMessage.contains("connection") ||
                    rootMessage.contains("timeout")) {
                return CustomErrorResponse.builder()
                        .code(DATABASE_CONNECTION_ERROR.getCode())
                        .message(DATABASE_CONNECTION_ERROR.getDefaultMessage())
                        .path(path)
                        .statusCode(DATABASE_CONNECTION_ERROR.getHttpStatus().value())
                        .logLevel("error")
                        .build();
            }

            if (rootCause instanceof ClassCastException || rootMessage.contains("mapping")) {
                return CustomErrorResponse.builder()
                        .code(MAPPING_ERROR.getCode())
                        .message(MAPPING_ERROR.getDefaultMessage())
                        .path(path)
                        .statusCode(MAPPING_ERROR.getHttpStatus().value())
                        .logLevel("error")
                        .build();
            }
        }

        return CustomErrorResponse.builder()
                .code(INTERNAL_SERVER_ERROR.getCode())
                .message(INTERNAL_SERVER_ERROR.getDefaultMessage())
                .path(path)
                .statusCode(INTERNAL_SERVER_ERROR.getHttpStatus().value())
                .logLevel("error")
                .build();
    }

    private static final Map<String, Integer> ERROR_CODE_TO_HTTP =
            Arrays.stream(DomainErrorCode.values())
                    .collect(Collectors.toMap(
                            DomainErrorCode::getCode,
                            DomainErrorCode::getHttpStatusCode
                    ));

    private HttpStatus mapDomainErrorToHttpStatus(String domainErrorCode) {
        Integer httpCode = ERROR_CODE_TO_HTTP.get(domainErrorCode);
        return httpCode != null ? HttpStatus.valueOf(httpCode) : HttpStatus.BAD_REQUEST;
    }

    private void logException(String method, String path, CustomErrorResponse errorResponse, Throwable ex) {
        switch (errorResponse.getLogLevel()) {
            case "info" -> log.info("Request: {} {} → {} ({})",
                    method, path, errorResponse.getStatusCode(), ex.getMessage());
            case "warn" -> log.warn("Client error: {} {} → {} - {}",
                    method, path, errorResponse.getStatusCode(), ex.getMessage());
            case "error" -> log.error("Server error: {} {} → {} - [{}]",
                    method, path, errorResponse.getStatusCode(), ex.getMessage(), ex);
        }
    }

    private String extractFieldName(jakarta.validation.Path propertyPath) {
        String fieldName = "";
        for (jakarta.validation.Path.Node node : propertyPath) {
            if (node.getName() != null) {
                fieldName = node.getName();
            }
        }
        return fieldName;
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }

    private String extractFieldPath(List<JsonMappingException.Reference> path) {
        if (path == null || path.isEmpty()) {
            return "unknown";
        }

        return path.stream()
                .map(ref -> ref.getFieldName() != null ? ref.getFieldName() : "[" + ref.getIndex() + "]")
                .filter(Objects::nonNull)
                .collect(Collectors.joining("."));
    }
    
    private String findFieldNameInExceptionChain(Throwable ex, String fallback) {
        Throwable current = ex;
        while (current != null) {
            if (current instanceof JsonMappingException jsonEx && jsonEx.getPath() != null) {
                String fieldName = extractFieldPath(jsonEx.getPath());
                if (!"unknown".equals(fieldName)) {
                    return fieldName;
                }
            }
            current = current.getCause();
        }
        return fallback;
    }

    private Mono<Void> buildResponse(ServerWebExchange exchange, Object body, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");

        try {
            //ObjectMapper objectMapper = new ObjectMapper();
           // objectMapper.findAndRegisterModules();
            String jsonBody = objectMapper.writeValueAsString(body);
            log.debug("Serialized response: {}", jsonBody);
            DataBuffer buffer = response.bufferFactory().wrap(jsonBody.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Error serializing response: {} - Body: {}", e.getMessage(), body);
            return response.setComplete();
        }
    }
}