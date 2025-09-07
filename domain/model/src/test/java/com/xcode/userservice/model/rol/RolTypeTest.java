package com.xcode.userservice.model.rol;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolTypeTest {

    @Test
    void shouldHaveCorrectEnumValues() {
        // When & Then
        assertEquals(3, RolType.values().length);
        
        // Verify all expected values exist
        assertNotNull(RolType.valueOf("ADMIN"));
        assertNotNull(RolType.valueOf("ASESOR"));
        assertNotNull(RolType.valueOf("CLIENTE"));
    }

    @Test
    void shouldHaveCorrectEnumNames() {
        // When & Then
        assertEquals("ADMIN", RolType.ADMIN.name());
        assertEquals("ASESOR", RolType.ASESOR.name());
        assertEquals("CLIENTE", RolType.CLIENTE.name());
    }

    @Test
    void shouldThrowExceptionForInvalidValue() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            RolType.valueOf("INVALID_ROLE")
        );
    }

    @Test
    void shouldSupportEnumComparison() {
        // Given
        RolType admin1 = RolType.ADMIN;
        RolType admin2 = RolType.ADMIN;
        RolType asesor = RolType.ASESOR;

        // When & Then
        assertEquals(admin1, admin2);
        assertNotEquals(admin1, asesor);
        assertTrue(admin1 == admin2); // Same enum constant
        assertFalse(admin1 == asesor);
    }
}