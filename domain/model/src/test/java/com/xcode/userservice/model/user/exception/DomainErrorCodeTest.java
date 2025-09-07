package com.xcode.userservice.model.user.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainErrorCodeTest {

    @Test
    void shouldHaveCorrectCodesAndMessages() {
        // Validaciones de códigos de error de usuario
        assertEquals("USR_001", DomainErrorCode.INVALID_EMAIL.getCode());
        assertEquals("The email isn't valid", DomainErrorCode.INVALID_EMAIL.getDefaultMessage());
        assertEquals(400, DomainErrorCode.INVALID_EMAIL.getHttpStatusCode());

        assertEquals("USR_002", DomainErrorCode.MISSING_REQUIRED_FIELD.getCode());
        assertEquals("The field is missing", DomainErrorCode.MISSING_REQUIRED_FIELD.getDefaultMessage());
        assertEquals(400, DomainErrorCode.MISSING_REQUIRED_FIELD.getHttpStatusCode());

        assertEquals("USR_003", DomainErrorCode.INVALID_SALARY.getCode());
        assertEquals("The salary is outside the allowed range.", DomainErrorCode.INVALID_SALARY.getDefaultMessage());
        assertEquals(400, DomainErrorCode.INVALID_SALARY.getHttpStatusCode());

        assertEquals("USR_004", DomainErrorCode.USER_ALREADY_EXISTS.getCode());
        assertEquals("The user already exists", DomainErrorCode.USER_ALREADY_EXISTS.getDefaultMessage());
        assertEquals(409, DomainErrorCode.USER_ALREADY_EXISTS.getHttpStatusCode());

        assertEquals("USR_005", DomainErrorCode.USER_NOT_ALLOWED.getCode());
        assertEquals("The user must be over 18 years old", DomainErrorCode.USER_NOT_ALLOWED.getDefaultMessage());
        assertEquals(400, DomainErrorCode.USER_NOT_ALLOWED.getHttpStatusCode());

        assertEquals("USR_006", DomainErrorCode.USER_NOT_FOUND.getCode());
        assertEquals("User not found", DomainErrorCode.USER_NOT_FOUND.getDefaultMessage());
        assertEquals(404, DomainErrorCode.USER_NOT_FOUND.getHttpStatusCode());
    }

    @Test
    void shouldHaveCorrectRoleErrorCodes() {
        // Validaciones de códigos de error de roles
        assertEquals("ROLE_001", DomainErrorCode.ROLE_NOT_FOUND.getCode());
        assertEquals("The role dont exists", DomainErrorCode.ROLE_NOT_FOUND.getDefaultMessage());
        assertEquals(404, DomainErrorCode.ROLE_NOT_FOUND.getHttpStatusCode());

        assertEquals("ROLE_002", DomainErrorCode.ROLE_NOT_ALLOWED.getCode());
        assertEquals("Role not allowed for user creation", DomainErrorCode.ROLE_NOT_ALLOWED.getDefaultMessage());
        assertEquals(403, DomainErrorCode.ROLE_NOT_ALLOWED.getHttpStatusCode());
    }

    @Test
    void shouldHaveUniqueErrorCodes() {
        // Verificar que no hay códigos duplicados
        DomainErrorCode[] errorCodes = DomainErrorCode.values();
        
        for (int i = 0; i < errorCodes.length; i++) {
            for (int j = i + 1; j < errorCodes.length; j++) {
                assertNotEquals(errorCodes[i].getCode(), errorCodes[j].getCode(),
                    "Duplicate error code found: " + errorCodes[i].getCode());
            }
        }
    }

    @Test
    void shouldHaveValidHttpStatusCodes() {
        // Verificar que todos los códigos HTTP son válidos
        for (DomainErrorCode errorCode : DomainErrorCode.values()) {
            int httpCode = errorCode.getHttpStatusCode();
            assertTrue(httpCode >= 400 && httpCode < 600,
                "Invalid HTTP status code for " + errorCode.name() + ": " + httpCode);
        }
    }

    @Test
    void shouldHaveNonEmptyMessages() {
        // Verificar que todos los mensajes no están vacíos
        for (DomainErrorCode errorCode : DomainErrorCode.values()) {
            assertNotNull(errorCode.getDefaultMessage(),
                "Message is null for " + errorCode.name());
            assertFalse(errorCode.getDefaultMessage().trim().isEmpty(),
                "Message is empty for " + errorCode.name());
        }
    }
}