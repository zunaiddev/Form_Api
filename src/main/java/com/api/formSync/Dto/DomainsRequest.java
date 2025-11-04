package com.api.formSync.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DomainsRequest {
    @NotNull
    private List<String> domains;
}