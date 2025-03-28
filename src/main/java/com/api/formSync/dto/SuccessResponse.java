package com.api.formSync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class SuccessResponse {
    private HttpStatus status;
    private String message;
    private Object data;

    public static SuccessResponse build(HttpStatus status, String message, Object data) {
        return new SuccessResponse(status, message, data);
    }
}
