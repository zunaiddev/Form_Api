package com.api.formSync.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;

@Data
public class ErrorResponse {
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
