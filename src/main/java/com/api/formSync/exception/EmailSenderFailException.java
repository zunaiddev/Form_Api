package com.api.formSync.exception;

public class EmailSenderFailException extends RuntimeException {
    public EmailSenderFailException(String message) {
        super("Unable To send Email");
    }

    public EmailSenderFailException() {
        super("Unable To send Email");
    }
}
