package com.api.formSync.exception;

public class UsedTokenException extends RuntimeException {
    public UsedTokenException(String message) {
        super(message);
    }
}
