package com.xcode.userservice.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorResponse {
    private String code;
    private String message;
    private Object data;
    private String path;
    
    @JsonIgnore
    private Integer statusCode;
    @JsonIgnore
    private String logLevel;

}