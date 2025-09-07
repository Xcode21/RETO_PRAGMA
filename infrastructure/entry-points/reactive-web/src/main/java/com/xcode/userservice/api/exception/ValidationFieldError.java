package com.xcode.userservice.api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationFieldError {
    private String field;
    private String message;
   // private Object rejectedValue;
}
