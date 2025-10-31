package com.api.formSync.dto;

import com.api.formSync.exception.CustomException;
import com.api.formSync.util.ErrorCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorRes {
    private HttpStatus status;
    private ErrorCode code;
    private String message;

    public ErrorRes(HttpStatus status, CustomException exp) {
        this.status = status;
        this.code = exp.getCode();
        this.message = exp.getLocalizedMessage();
    }

    public ErrorRes(HttpStatus status, ErrorCode code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}