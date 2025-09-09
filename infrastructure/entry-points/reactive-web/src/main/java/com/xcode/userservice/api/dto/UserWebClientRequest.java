package com.xcode.userservice.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWebClientRequest {

    @NotBlank(message = "El documento es obligatorio")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "El documento debe tener entre 8 y 15 dígitos numéricos")
    private String document;

}
