package com.xcode.userservice.model.rol;

import com.xcode.userservice.model.user.exception.MissingRequiredFieldException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void shouldCreateRoleSuccessfully() {
        // Given
        Integer idRol = 1;
        RolType type = RolType.ADMIN;
        String description = "Administrator role";

        // When
        Role role = new Role(idRol, type, description);

        // Then
        assertNotNull(role);
        assertEquals(idRol, role.getIdRol());
        assertEquals(type, role.getType());
        assertEquals(description, role.getDescription());
    }

    @Test
    void shouldCreateRoleWithBuilderSuccessfully() {
        // Given
        Integer idRol = 2;
        RolType type = RolType.ASESOR;
        String description = "Advisor role";

        // When
        Role role = Role.builder()
                .idRol(idRol)
                .type(type)
                .description(description)
                .build();

        // Then
        assertNotNull(role);
        assertEquals(idRol, role.getIdRol());
        assertEquals(type, role.getType());
        assertEquals(description, role.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenIdRolIsNull() {
        // Given
        Integer idRol = null;
        RolType type = RolType.ADMIN;
        String description = "Administrator role";

        // When & Then
        assertThrows(MissingRequiredFieldException.class, () ->
            new Role(idRol, type, description)
        );
    }

    @Test
    void shouldTrimDescriptionWhenNotNull() {
        // Given
        Integer idRol = 1;
        RolType type = RolType.ADMIN;
        String description = "  Administrator role  ";

        // When
        Role role = new Role(idRol, type, description);

        // Then
        assertEquals("Administrator role", role.getDescription());
    }

    @Test
    void shouldHandleNullDescription() {
        // Given
        Integer idRol = 1;
        RolType type = RolType.ADMIN;
        String description = null;

        // When
        Role role = new Role(idRol, type, description);

        // Then
        assertNull(role.getDescription());
    }

    @Test
    void shouldReturnTrueForAdminRole() {
        // Given
        Role adminRole = Role.builder()
                .idRol(1)
                .type(RolType.ADMIN)
                .build();

        // When & Then
        assertTrue(adminRole.isAdmin());
        assertFalse(adminRole.isAsesor());
        assertFalse(adminRole.isCliente());
    }

    @Test
    void shouldReturnTrueForAsesorRole() {
        // Given
        Role asesorRole = Role.builder()
                .idRol(2)
                .type(RolType.ASESOR)
                .build();

        // When & Then
        assertFalse(asesorRole.isAdmin());
        assertTrue(asesorRole.isAsesor());
        assertFalse(asesorRole.isCliente());
    }

    @Test
    void shouldReturnTrueForClienteRole() {
        // Given
        Role clienteRole = Role.builder()
                .idRol(3)
                .type(RolType.CLIENTE)
                .build();

        // When & Then
        assertFalse(clienteRole.isAdmin());
        assertFalse(clienteRole.isAsesor());
        assertTrue(clienteRole.isCliente());
    }

    @Test
    void shouldHandleNullType() {
        // Given
        Role roleWithNullType = Role.builder()
                .idRol(1)
                .type(null)
                .build();

        // When & Then
        assertFalse(roleWithNullType.isAdmin());
        assertFalse(roleWithNullType.isAsesor());
        assertFalse(roleWithNullType.isCliente());
    }

    @Test
    void shouldCreateRoleWithToBuilder() {
        // Given
        Role originalRole = Role.builder()
                .idRol(1)
                .type(RolType.ADMIN)
                .description("Original description")
                .build();

        // When
        Role modifiedRole = originalRole.toBuilder()
                .description("Modified description")
                .build();

        // Then
        assertEquals(originalRole.getIdRol(), modifiedRole.getIdRol());
        assertEquals(originalRole.getType(), modifiedRole.getType());
        assertEquals("Modified description", modifiedRole.getDescription());
        // Original should remain unchanged
        assertEquals("Original description", originalRole.getDescription());
    }
}