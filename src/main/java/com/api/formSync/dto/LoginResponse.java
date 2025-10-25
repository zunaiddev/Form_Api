package com.api.formSync.dto;

import com.api.formSync.util.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserStatus status;
}
