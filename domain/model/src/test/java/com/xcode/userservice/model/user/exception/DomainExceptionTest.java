package com.xcode.userservice.model.user.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    @Test
    void shouldCreateDomainExceptionWithErrorCode() {
        // Given
        DomainErrorCode errorCode = DomainErrorCode.USER_ALREADY_EXISTS;

        // When
        DomainException exception = new DomainException(errorCode);

        // Then
        assertEquals(errorCode.getCode(), exception.getErrorCode());
        assertEquals(errorCode.getDefaultMessage(), exception.getErrorMessage());
        assertEquals(errorCode.getDefaultMessage(), exception.getMessage());
    }

    @Test
    void shouldCreateDomainExceptionWithCustomMessage() {
        // Given
        DomainErrorCode errorCode = DomainErrorCode.INVALID_EMAIL;
        String customMessage = "Custom error message";
        String expectedMessage = errorCode.getDefaultMessage() + ": " + customMessage;

        // When
        DomainException exception = new DomainException(errorCode, customMessage);

        // Then
        assertEquals(errorCode.getCode(), exception.getErrorCode());
        assertEquals(expectedMessage, exception.getErrorMessage());
        assertEquals(expectedMessage, exception.getMessage());
    }
}