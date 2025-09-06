package com.xcode.userservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 100, message = "Los nombres deben tener entre 2 y 100 caracteres")
    @Schema(description = "Nombres del usuario", example = "Juan Carlos")
    private String firstName;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
    @Schema(description = "Apellidos del usuario", example = "Pérez Gómez")
    private String lastName;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")

    private LocalDate birthDate;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    @Schema(description = "Dirección actual del usuario", example = "Av. Siempre Viva 742")
    private String address;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9+\\-\\s()]{7,15}$", message = "Formato de teléfono inválido")
    @Schema(description = "Número de teléfono", example = "+51 999888777")
    private String phone;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    @Schema(description = "Correo electrónico válido", example = "juan.perez@email.com")
    private String email;

    @NotBlank(message = "El documento es obligatorio")
    @Size(min = 1, max = 15, message = "El documento debe tener entre 1 y 15 caracteres")
    @Schema(description = "Documento de identidad", example = "12345678")
    private String documento;

    @NotNull(message = "El salario base es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El salario debe ser mayor a cero")
    @DecimalMax(value = "1500001", inclusive = false, message = "El salario debe ser menor o giual a 15,000,000")
    @Schema(description = "Salario base del usuario", type = "number", format = "decimal", example = "2500.75")
    Double baseSalary;

    @NotNull(message = "El tipo de préstamo es obligatorio")
    @Schema(description = "Rol del usuario (1=Admin, 2=User)", example = "1")
    private Integer rol;
}
