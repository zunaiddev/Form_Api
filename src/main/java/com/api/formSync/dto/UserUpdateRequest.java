package com.api.formSync.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateRequest {
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Invalid name format.")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid Email.")
    private String email;

    private String currentPassword;

    @Pattern(regexp = "^(?!.*\\\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$&*]).{8,20}$",
            message = "password length must be between 8 and 20 and contain at least 1 uppercase, 1 lowercase, 1 special character, and 1 digit")
    private String password;

    public boolean isValid() {
        return name != null ||
                (email != null && currentPassword != null) ||
                (password != null && currentPassword != null);
    }
}