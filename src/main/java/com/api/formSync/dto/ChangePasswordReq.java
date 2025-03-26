package com.api.formSync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordReq {
    @NotEmpty
    String currentPassword;

    @NotBlank(message = "new password is null or blank")
    @Pattern(regexp = "^(?!.*\\\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$&*]).{8,20}$",
            message = "new password length must be between 8 and 20 and contain at least 1 uppercase, 1 lowercase, 1 special character, and 1 digit")
    String newPassword;
}
