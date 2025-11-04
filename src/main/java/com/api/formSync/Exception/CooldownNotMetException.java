package com.api.formSync.Exception;

public class CooldownNotMetException extends RuntimeException {
    public CooldownNotMetException(String message) {
        super(message);
    }
}