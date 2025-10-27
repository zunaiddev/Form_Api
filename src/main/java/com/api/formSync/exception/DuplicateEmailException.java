package com.api.formSync.exception;

import com.api.formSync.util.ErrorCode;

public class DuplicateEmailException extends CustomException {
    public DuplicateEmailException(String email) {
        super(ErrorCode.DUPLICATE_EMAIL, String.format("User with %s already exists.", email));
    }
}