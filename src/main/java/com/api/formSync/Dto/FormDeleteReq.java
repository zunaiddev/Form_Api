package com.api.formSync.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FormDeleteReq {
    @NotNull
    List<Long> id;
}
