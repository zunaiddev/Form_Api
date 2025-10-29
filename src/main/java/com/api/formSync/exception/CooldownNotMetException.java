package com.api.formSync.exception;

public class CooldownNotMetException extends RuntimeException {
    public CooldownNotMetException(String message) {
        super(message);
    }
}