package com.api.formSync.Exception;

import com.api.formSync.util.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode code;

    public CustomException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}