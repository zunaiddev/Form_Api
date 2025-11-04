package com.api.formSync.Exception;

import org.springframework.security.core.AuthenticationException;

public class UnauthorisedException extends AuthenticationException {
    public UnauthorisedException(String message) {
        super(message);
    }
}