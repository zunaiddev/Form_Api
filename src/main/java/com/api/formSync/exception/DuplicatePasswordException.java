package com.api.formSync.exception;

public class DuplicatePasswordException extends RuntimeException {
    public DuplicatePasswordException(String message) {
        super(message);
    }
}
