package com.xcode.userservice.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name="users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column("id")
    private UUID idUser;

    @Column("nombres")
    private String firstName;

    @Column("apellidos")
    private String lastName;

    @Column("fecha_nacimiento")
    private LocalDate birthDate;

    @Column("direccion")
    private String address;

    @Column("telefono")
    private String phone;

    @Column("email")
    private String email;

    @Column("documento")
    private String document;

    @Column("salario_base")
    private Double salaryBase;

    @Column("id_rol")
    private Integer rol;

    @Column("password")
    private String password;

    @Column("fecha_creacion")
    private LocalDateTime createdAt;

    @Column("fecha_actualizacion")
    private LocalDateTime updatedAt;

}
