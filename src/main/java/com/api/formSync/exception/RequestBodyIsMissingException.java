package com.api.formSync.exception;

public class RequestBodyIsMissingException extends RuntimeException {
    public RequestBodyIsMissingException(String message) {
        super(message);
    }
}
