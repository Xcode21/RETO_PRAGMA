package com.xcode.userservice.model.user;

import com.xcode.userservice.model.rol.Role;
import com.xcode.userservice.model.user.exception.InvalidEmailException;
import com.xcode.userservice.model.user.exception.InvalidSalaryException;
import com.xcode.userservice.model.user.exception.MissingRequiredFieldException;
import com.xcode.userservice.model.user.exception.UserNotAllowedException;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@Getter // Solo getters, inmutable
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Constructor privado
@NoArgsConstructor(access = AccessLevel.PRIVATE) // Para frameworks
public class User {

    private UUID idUser;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    private String document;
    private Double salaryBase;
    private Role role;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static User createNew(String firstName, String lastName, LocalDate birthDate, String address, String phone, String email, String document, Double salaryBase, Role role,String password) {

        if (firstName == null || firstName.isBlank()) {
            throw new MissingRequiredFieldException("firstName");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new MissingRequiredFieldException("lastName");
        }
        if (salaryBase < 0 || salaryBase > 15000000) {
            throw new InvalidSalaryException(salaryBase);
        }
        if (email == null || email.isBlank()) {
            throw new MissingRequiredFieldException("Email");
        }
        if (document == null || document.isBlank()) {
            throw new MissingRequiredFieldException("Document");
        }
        if (!isAdult(birthDate)) {
            throw new UserNotAllowedException(birthDate.toString());
        }
        if (!isEmailValid(email)) {
            throw new InvalidEmailException(email);
        }
        if (role == null) {
            throw new MissingRequiredFieldException("role");
        }

        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .address(address)
                .email(email)
                .document(document)
                .phone(phone)
                .role(role)
                .salaryBase(salaryBase)
                .password(password)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static User fromRepository(UUID idUser, String name, String lastName,LocalDate birthDate,String address, String email,
                                      String document, String phone, Role role, Double baseSalary,String password,
                                      LocalDateTime createdAt) {

        return User.builder()
                .idUser(idUser)
                .firstName(name)
                .lastName(lastName)
                .birthDate(birthDate)
                .address(address)
                .email(email)
                .document(document)
                .phone(phone)
                .role(role)
                .salaryBase(baseSalary)
                .password(password)
                .createdAt(createdAt)
                .build();
    }

    public static boolean isEmailValid(String value) {
        return value != null && value.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private static boolean isAdult(LocalDate birthDate) {
        return birthDate.plusYears(18).isBefore(LocalDate.now()) ||
                birthDate.plusYears(18).isEqual(LocalDate.now());
    }

}
