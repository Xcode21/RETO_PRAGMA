package com.xcode.userservice.model.user.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleNotFoundExceptionTest {

    @Test
    void shouldCreateRoleNotFoundExceptionWithRoleId() {
        // Given
        Integer roleId = 1;

        // When
        RoleNotFoundException exception = new RoleNotFoundException(roleId);

        // Then
        assertThat(exception.getErrorCode()).isEqualTo(DomainErrorCode.ROLE_NOT_FOUND.getCode());
        assertThat(exception.getMessage()).isEqualTo("The role dont exists: Rol not found with ID: " + roleId);
        assertThat(exception).isInstanceOf(DomainException.class);
    }
}