package com.xcode.userservice.model.user.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserNotFoundExceptionTest {

    @Test
    void shouldCreateUserNotFoundExceptionWithUserId() {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        UserNotFoundException exception = new UserNotFoundException(userId);

        // Then
        assertThat(exception.getErrorCode()).isEqualTo(DomainErrorCode.USER_NOT_FOUND.getCode());
        assertThat(exception.getMessage()).isEqualTo("User not found: User not found with ID: " + userId);
        assertThat(exception).isInstanceOf(DomainException.class);
    }

    @Test
    void shouldCreateUserNotFoundExceptionWithCustomMessage() {
        // Given
        String customMessage = "Custom user not found message";

        // When
        UserNotFoundException exception = new UserNotFoundException(customMessage);

        // Then
        assertThat(exception.getErrorCode()).isEqualTo(DomainErrorCode.USER_NOT_FOUND.getCode());
        assertThat(exception.getMessage()).isEqualTo("User not found: " + customMessage);
        assertThat(exception).isInstanceOf(DomainException.class);
    }
}