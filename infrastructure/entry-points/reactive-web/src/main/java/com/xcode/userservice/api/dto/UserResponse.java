package com.xcode.userservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con los datos del usuario creado")
public class UserResponse {
    @Schema(description = "Identificador único del usuario", example = "550e8400-e29b-41d4-a716-446655440000")
    private String idUser;
    
    @Schema(description = "Nombres del usuario", example = "Juan Carlos")
    private String firstName;
    
    @Schema(description = "Apellidos del usuario", example = "Pérez Gómez")
    private String lastName;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de nacimiento", example = "1990-05-15", type = "string", format = "date")
    private LocalDate birthDate;
    
    @Schema(description = "Dirección del usuario", example = "Av. Siempre Viva 742")
    private String address;
    
    @Schema(description = "Número de teléfono", example = "+51 999888777")
    private String phone;
    
    @Schema(description = "Correo electrónico", example = "juan.perez@email.com")
    private String email;
    
    @Schema(description = "Documento de identidad", example = "12345678")
    private String documento;
    
    @Schema(description = "Salario base", example = "2500.75", type = "number", format = "double")
    private Double baseSalary;
    
    @Schema(description = "Nombre del rol asignado", example = "Administrador")
    private String roleName;
}