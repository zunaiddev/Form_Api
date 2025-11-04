package com.api.formSync.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResendTokenResponse {
    private int status;
    private String message;
    private UserDTO userDTO;
}
