package com.api.formSync.dto;

import com.api.formSync.util.TextFormatter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupRequest {
    @NotBlank(message = "Name is null or blank")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$",
            message = "Name must contain only letters, spaces, hyphens, or apostrophes")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @JsonDeserialize(using = TextFormatter.class)
    private String name;

    @NotBlank(message = "Email is null or blank")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email must be a valid format (e.g., user@example.com)")
    @JsonDeserialize(using = TextFormatter.class)
    private String email;

    @NotBlank(message = "password is null or blank")
    @Pattern(regexp = "^(?!.*\\\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$&*]).{8,20}$",
            message = "password length must be between 8 and 20 and contain at least 1 uppercase, 1 lowercase, 1 special character, and 1 digit")
    private String password;
}