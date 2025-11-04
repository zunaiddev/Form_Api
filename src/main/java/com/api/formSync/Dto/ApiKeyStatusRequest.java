package com.api.formSync.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyStatusRequest {
    @NotNull(message = "Status must not be null")
    private Boolean active;
}
