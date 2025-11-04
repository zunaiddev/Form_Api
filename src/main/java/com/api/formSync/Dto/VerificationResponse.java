package com.api.formSync.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerificationResponse<T> {
    public String title;
    public String message;
    public T extra;
}