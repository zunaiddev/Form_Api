package com.api.formSync.Dto;

import com.api.formSync.util.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class SignInResponse {
    private String token;
    private UserStatus status;
    private Instant deleteAt;
}
