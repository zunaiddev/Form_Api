package com.api.formSync.Exception;

public class UsedTokenException extends RuntimeException {
    public UsedTokenException(String message) {
        super(message);
    }
}