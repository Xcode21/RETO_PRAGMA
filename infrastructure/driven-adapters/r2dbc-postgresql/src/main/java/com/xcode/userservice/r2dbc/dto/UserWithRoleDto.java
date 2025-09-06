package com.xcode.userservice.r2dbc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserWithRoleDto (
        UUID id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String address,
        String phone,
        String email,
        String document,
        Double salaryBase,
        String rolNombre,
        LocalDateTime fechaCreacion
) {}
