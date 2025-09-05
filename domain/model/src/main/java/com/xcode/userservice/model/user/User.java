package com.xcode.userservice.model.user;
import com.xcode.userservice.model.rol.Rol;
import com.xcode.userservice.model.user.exception.InvalidEmailException;
import com.xcode.userservice.model.user.exception.InvalidSalaryException;
import com.xcode.userservice.model.user.exception.MissingRequiredFieldException;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class User {

    private final UUID idUser;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;
    private final String address;
    private final String phone;
    private final String email;
    private final String document;
    private final Double salaryBase;
    private final Rol rol;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private User(UUID idUser, String firstName, String lastName, LocalDate birthDate, String address, String phone, String email, String document, Double salaryBase,Rol rol) {

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
        if(!isEmailValid(email)) {
            throw new InvalidEmailException(email);
        }
        if (rol == null) {
            throw new MissingRequiredFieldException("rol");
        }
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.document = document;
        this.salaryBase = salaryBase;
        this.rol = rol;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isEmailValid(String value) {
        return value != null && value.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

}
