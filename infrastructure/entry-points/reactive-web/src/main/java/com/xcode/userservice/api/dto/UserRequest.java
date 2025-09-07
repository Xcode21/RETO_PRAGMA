package com.xcode.userservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @NotBlank(message = "Names are required")
    @Size(min = 2, max = 100, message = "Names must be between 2 and 100 characters")
    @Schema(description = "Nombres del usuario", example = "Juan Carlos")
    private String firstName;

    @NotBlank(message = "Last names are required")
    @Size(min = 2, max = 100, message = "Last names must be between 2 and 100 characters long.")
    @Schema(description = "Apellidos del usuario", example = "Pérez Gómez")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "The date of birth must be before today")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "The address cannot exceed 255 characters.")
    @Schema(description = "Dirección actual del usuario", example = "Av. Siempre Viva 742")
    private String address;

    @NotBlank(message = "Telephone is required")
    @Pattern(regexp = "^[0-9+\\-\\s()]{7,15}$", message = "Invalid phone format")
    @Schema(description = "Número de teléfono", example = "+51 999888777")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Correo electrónico válido", example = "juan.perez@email.com")
    private String email;

    @NotBlank(message = "The document is mandatory")
    @Size(min = 1, max = 15, message = "The document must have between 1 and 15 characters")
    @Schema(description = "Documento de identidad", example = "12345678")
    private String documento;

    @NotNull(message = "Base salary is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "The salary must be greater than zero")
    @DecimalMax(value = "1500001", inclusive = false, message = "The salary must be less than or equal to 15,000,000")
    @Schema(description = "Salario base del usuario", type = "number", format = "decimal", example = "2500.75")
    private Double baseSalary;

    @NotNull(message = "The role is mandatory")
    @Min(value = 1, message = "Role ID must be positive")
    @Schema(description = "Rol del usuario (1=Admin, 2=User)", example = "1")
    private Integer rol;

    @NotNull(message = "Password is required")
    @Schema(description = "Password de inicio se session")
    private String password;
}
