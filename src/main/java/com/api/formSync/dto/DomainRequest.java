package com.api.formSync.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DomainRequest {
    @Pattern(regexp = "^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid domain format")
    @NotNull
    private String domain;
}
