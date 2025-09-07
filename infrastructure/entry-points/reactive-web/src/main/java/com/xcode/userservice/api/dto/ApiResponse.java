package com.xcode.userservice.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta estándar de la API")
public class ApiResponse<T> {
    @Schema(description = "Código de respuesta", example = "INFRA-004")
    private String code;
    
    @Schema(description = "Mensaje descriptivo de la operación", example = "Usuario creado exitosamente")
    private String message;
    
    @Schema(description = "Datos de respuesta")
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code("INFRA-004")
                .message("Operación exitosa")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .code("INFRA-004")
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> validationError(String message) {
        return ApiResponse.<T>builder()
                .code("VALIDATION_ERROR")
                .message(message)
                .build();
    }
}