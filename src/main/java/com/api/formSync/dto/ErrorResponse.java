package com.api.formSync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String status;
    private String message;
    private Map<String, Object> error;

    public static ErrorResponse build(String message, int code, String details) {
        return new ErrorResponse("error", message, Map.of("code", code, "details", details));
    }
}