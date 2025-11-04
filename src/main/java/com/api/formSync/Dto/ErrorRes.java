package com.api.formSync.Dto;

import com.api.formSync.Exception.CustomException;
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