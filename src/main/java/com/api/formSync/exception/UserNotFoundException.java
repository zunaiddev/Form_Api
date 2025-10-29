package com.api.formSync.exception;

import com.api.formSync.util.ErrorCode;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND, "User Not Found");
    }
}