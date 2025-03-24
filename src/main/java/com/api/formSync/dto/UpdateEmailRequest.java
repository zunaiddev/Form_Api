package com.api.formSync.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateEmailRequest {
    @Pattern(regexp = "", message = "Invalid Email Format.")
    private String email;
    @Pattern(regexp = "", message = "Invalid Password.")
    private String password;
}
