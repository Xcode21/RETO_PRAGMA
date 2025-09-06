package com.xcode.userservice.model.rol;
import com.xcode.userservice.model.user.exception.MissingRequiredFieldException;
import lombok.Builder;
import lombok.Getter;
//import lombok.NoArgsConstructor;


@Getter
@Builder(toBuilder = true)
public class Role {
    private final Integer idRol;
    private final RolType type;
    private final String description;

    public Role(Integer idRol, RolType type, String description) {
        if (type == null) {
            throw new MissingRequiredFieldException("Role type");
        }
        this.idRol = idRol;
        this.type = type;
        this.description = description != null ? description.trim() : null;
    }

    public boolean isAdmin() {
        return RolType.ADMIN.equals(this.type);
    }

    public boolean isAsesor() {
        return RolType.ASESOR.equals(this.type);
    }

    public boolean isCliente() {
        return RolType.CLIENTE.equals(this.type);
    }
}
