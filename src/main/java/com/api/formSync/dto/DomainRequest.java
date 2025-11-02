package com.api.formSync.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DomainRequest {
    @NotBlank(message = "domain must not be blank")
    private String domain;
}
