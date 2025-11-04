package com.api.formSync.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username is missing")
    @Email(message = "Invalid username")
    private String email;

    @NotBlank(message = "Invalid password")
    private String password;
}