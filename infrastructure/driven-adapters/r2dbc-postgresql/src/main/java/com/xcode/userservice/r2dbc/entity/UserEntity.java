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

@Table(name="user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column("id")
    private UUID idUser;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("addres")
    private String address;

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;

    @Column("document")
    private String document;

    @Column("salary_base")
    private Double salaryBase;

    @Column("id_role")
    private Integer role;

    @Column("create_at")
    private LocalDateTime createdAt;

    @Column("update_at")
    private LocalDateTime updatedAt;

}
