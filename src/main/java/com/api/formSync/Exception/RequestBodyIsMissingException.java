package com.api.formSync.Exception;

public class RequestBodyIsMissingException extends RuntimeException {
    public RequestBodyIsMissingException(String message) {
        super(message);
    }
}