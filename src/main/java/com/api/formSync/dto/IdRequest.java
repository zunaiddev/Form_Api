package com.api.formSync.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdRequest {
    @NotNull
    private Long id;
}
