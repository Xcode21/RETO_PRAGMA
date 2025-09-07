package com.xcode.userservice.model.user.exception;

import java.util.UUID;

public class RoleNotFoundException extends DomainException{
    public RoleNotFoundException(Integer rolId) {
        super(DomainErrorCode.ROLE_NOT_FOUND, "Rol not found with ID: " + rolId);
    }

}
