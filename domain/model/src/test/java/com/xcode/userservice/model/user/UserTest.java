package com.xcode.userservice.model.user;

import com.xcode.userservice.model.rol.Role;
import com.xcode.userservice.model.rol.RolType;
import com.xcode.userservice.model.user.exception.InvalidEmailException;
import com.xcode.userservice.model.user.exception.InvalidSalaryException;
import com.xcode.userservice.model.user.exception.MissingRequiredFieldException;
import com.xcode.userservice.model.user.exception.UserNotAllowedException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateNewUserSuccessfully() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        String phone = "+1234567890";
        String email = "john.doe@example.com";
        String document = "12345678";
        Double salaryBase = 50000.0;
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        String password = "password123";

        // When
        User user = User.createNew(firstName, lastName, birthDate, address, phone, email, document, salaryBase, role, password);

        // Then
        assertNotNull(user);
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(birthDate, user.getBirthDate());
        assertEquals(address, user.getAddress());
        assertEquals(phone, user.getPhone());
        assertEquals(email, user.getEmail());
        assertEquals(document, user.getDocument());
        assertEquals(salaryBase, user.getSalaryBase());
        assertEquals(role, user.getRole());
        assertEquals(password, user.getPassword());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void shouldThrowExceptionWhenFirstNameIsNull() {
        // Given
        String firstName = null;
        String lastName = "Doe";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        String phone = "+1234567890";
        String email = "john.doe@example.com";
        String document = "12345678";
        Double salaryBase = 50000.0;
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        String password = "password123";

        // When & Then
        assertThrows(MissingRequiredFieldException.class, () ->
            User.createNew(firstName, lastName, birthDate, address, phone, email, document, salaryBase, role, password)
        );
    }

    @Test
    void shouldThrowExceptionWhenFirstNameIsBlank() {
        // Given
        String firstName = "";
        String lastName = "Doe";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        String phone = "+1234567890";
        String email = "john.doe@example.com";
        String document = "12345678";
        Double salaryBase = 50000.0;
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        String password = "password123";

        // When & Then
        assertThrows(MissingRequiredFieldException.class, () ->
            User.createNew(firstName, lastName, birthDate, address, phone, email, document, salaryBase, role, password)
        );
    }

    @Test
    void shouldThrowExceptionWhenLastNameIsNull() {
        // Given
        String firstName = "John";
        String lastName = null;
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        String phone = "+1234567890";
        String email = "john.doe@example.com";
        String document = "12345678";
        Double salaryBase = 50000.0;
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        String password = "password123";

        // When & Then
        assertThrows(MissingRequiredFieldException.class, () ->
            User.createNew(firstName, lastName, birthDate, address, phone, email, document, salaryBase, role, password)
        );
    }

    @Test
    void shouldThrowExceptionWhenSalaryIsNegative() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        String phone = "+1234567890";
        String email = "john.doe@example.com";
        String document = "12345678";
        Double salaryBase = -1000.0;
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        String password = "password123";

        // When & Then
        assertThrows(InvalidSalaryException.class, () ->
            User.createNew(firstName, lastName, birthDate, address, phone, email, document, salaryBase, role, password)
        );
    }

    @Test
    void shouldThrowExceptionWhenSalaryExceedsMaximum() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        String phone = "+1234567890";
        String email = "john.doe@example.com";
        String document = "12345678";
        Double salaryBase = 16000000.0;
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        String password = "password123";

        // When & Then
        assertThrows(InvalidSalaryException.class, () ->
            User.createNew(firstName, lastName, birthDate, address, phone, email, document, salaryBase, role, password)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        String phone = "+1234567890";
        String email = "invalid-email";
        String document = "12345678";
        Double salaryBase = 50000.0;
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        String password = "password123";

        // When & Then
        assertThrows(InvalidEmailException.class, () ->
            User.createNew(firstName, lastName, birthDate, address, phone, email, document, salaryBase, role, password)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIsUnder18() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        LocalDate birthDate = LocalDate.now().minusYears(17); // Menor de 18
        String address = "123 Main St";
        String phone = "+1234567890";
        String email = "john.doe@example.com";
        String document = "12345678";
        Double salaryBase = 50000.0;
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        String password = "password123";

        // When & Then
        assertThrows(UserNotAllowedException.class, () ->
            User.createNew(firstName, lastName, birthDate, address, phone, email, document, salaryBase, role, password)
        );
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNull() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        String phone = "+1234567890";
        String email = "john.doe@example.com";
        String document = "12345678";
        Double salaryBase = 50000.0;
        Role role = null;
        String password = "password123";

        // When & Then
        assertThrows(MissingRequiredFieldException.class, () ->
            User.createNew(firstName, lastName, birthDate, address, phone, email, document, salaryBase, role, password)
        );
    }

    @Test
    void shouldCreateUserFromRepositorySuccessfully() {
        // Given
        UUID idUser = UUID.randomUUID();
        String firstName = "John";
        String lastName = "Doe";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        String address = "123 Main St";
        String email = "john.doe@example.com";
        String document = "12345678";
        String phone = "+1234567890";
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        Double salaryBase = 50000.0;
        String password = "password123";
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        User user = User.fromRepository(idUser, firstName, lastName, birthDate, address, email, document, phone, role, salaryBase, password, createdAt);

        // Then
        assertNotNull(user);
        assertEquals(idUser, user.getIdUser());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(birthDate, user.getBirthDate());
        assertEquals(address, user.getAddress());
        assertEquals(email, user.getEmail());
        assertEquals(document, user.getDocument());
        assertEquals(phone, user.getPhone());
        assertEquals(role, user.getRole());
        assertEquals(salaryBase, user.getSalaryBase());
        assertEquals(password, user.getPassword());
        assertEquals(createdAt, user.getCreatedAt());
    }

    @Test
    void shouldValidateEmailCorrectly() {
        // Valid emails
        assertTrue(User.isEmailValid("user@example.com"));
        assertTrue(User.isEmailValid("test.user@domain.co.uk"));
        assertTrue(User.isEmailValid("user123@test-domain.com"));

        // Invalid emails
        assertFalse(User.isEmailValid("invalid-email"));
        assertFalse(User.isEmailValid("@domain.com"));
        assertFalse(User.isEmailValid("user@"));
        assertFalse(User.isEmailValid("user.domain.com"));
        assertFalse(User.isEmailValid(null));
    }

    @Test
    void shouldThrowMissingRequiredFieldExceptionWhenEmailIsNull() {
        // Given
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        // When & Then
        MissingRequiredFieldException exception = assertThrows(MissingRequiredFieldException.class, () ->
                User.createNew("John", "Doe", birthDate, "123 Main St", "+1234567890", 
                             null, "12345678", 50000.0, role, "password123"));

        assertEquals("The field is missing: Email", exception.getMessage());
    }

    @Test
    void shouldThrowMissingRequiredFieldExceptionWhenEmailIsBlank() {
        // Given
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        // When & Then
        MissingRequiredFieldException exception = assertThrows(MissingRequiredFieldException.class, () ->
                User.createNew("John", "Doe", birthDate, "123 Main St", "+1234567890", 
                             "   ", "12345678", 50000.0, role, "password123"));

        assertEquals("The field is missing: Email", exception.getMessage());
    }

    @Test
    void shouldThrowMissingRequiredFieldExceptionWhenDocumentIsNull() {
        // Given
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        // When & Then
        MissingRequiredFieldException exception = assertThrows(MissingRequiredFieldException.class, () ->
                User.createNew("John", "Doe", birthDate, "123 Main St", "+1234567890", 
                             "john@example.com", null, 50000.0, role, "password123"));

        assertEquals("The field is missing: Document", exception.getMessage());
    }

    @Test
    void shouldThrowMissingRequiredFieldExceptionWhenDocumentIsBlank() {
        // Given
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        // When & Then
        MissingRequiredFieldException exception = assertThrows(MissingRequiredFieldException.class, () ->
                User.createNew("John", "Doe", birthDate, "123 Main St", "+1234567890", 
                             "john@example.com", "   ", 50000.0, role, "password123"));

        assertEquals("The field is missing: Document", exception.getMessage());
    }

    @Test
    void shouldAllowUserWithExactly18YearsOld() {
        // Given - Persona que cumple exactamente 18 años hoy
        LocalDate exactlyEighteenToday = LocalDate.now().minusYears(18);
        Role role = Role.builder().idRol(1).type(RolType.ADMIN).build();

        // When & Then - No debe lanzar excepción
        assertDoesNotThrow(() ->
                User.createNew("John", "Doe", exactlyEighteenToday, "123 Main St", "+1234567890", 
                             "john@example.com", "12345678", 50000.0, role, "password123"));
    }
}