package com.api.formSync.dto;

import com.api.formSync.util.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponse {
    private String token;
    private UserStatus status;
}
