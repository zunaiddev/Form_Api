package com.api.formSync.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequest {
    @NotNull(message = "Id can not be null")
    private Long id;
}