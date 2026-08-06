// src/main/java/com/pms/auth/dto/RegisterRequest.java
package com.pms.auth.dto;

import com.pms.auth.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String fullName;

    @NotBlank @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private Role role;
}