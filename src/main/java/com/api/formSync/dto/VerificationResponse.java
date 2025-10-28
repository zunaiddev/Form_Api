package com.api.formSync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerificationResponse<T> {
    public String title;
    public String message;
    public T extra;
}