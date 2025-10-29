package com.api.formSync.exception;

import com.api.formSync.util.ErrorCode;

public class ConflictException extends CustomException {
    public ConflictException(String message) {
        super(ErrorCode.CONFLICT, message);
    }
}