package com.xcode.userservice.r2dbc.exception;

public enum InfraErrorCode {
    DATABASE_ERROR("INFRA-001", "Error al acceder a la base de datos"),
    DB_CONNECTION_TIMEOUT("INFRA-002", "Tiempo de espera agotado al conectar con la base de datos"),
    DB_DUPLICATE_KEY("INFRA-003", "Violación de clave única en la base de datos"),
    MESSAGING_ERROR("INFRA-004", "Error en la mensajería"),
    UNKNOWN_INFRA_ERROR("INFRA-999", "Error de infraestructura desconocido");

    private final String code;
    private final String message;

    InfraErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
