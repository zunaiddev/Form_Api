package com.api.formSync.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEntrypointEmailException extends RuntimeException {
    public DuplicateEntrypointEmailException(String message) {
        super(message);
    }
}
