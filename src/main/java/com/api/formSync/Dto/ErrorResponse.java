package com.api.formSync.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String status;
    private String message;
    private Map<String, Object> error;

    public static ErrorResponse build(String message, HttpStatus status, String details) {
        return new ErrorResponse("error", message, Map.of("code", status.value(), "details", details));
    }
}