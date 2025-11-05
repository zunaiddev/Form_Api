package com.api.formSync.Dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateRequest {
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Invalid name format.")
    private String name;
}