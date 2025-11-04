package com.api.formSync.Exception;

import com.api.formSync.util.ErrorCode;

public class CouldNotFoundTokenException extends CustomException {
    public CouldNotFoundTokenException(String message) {
        super(ErrorCode.MISSING_TOKEN, message);
    }
}