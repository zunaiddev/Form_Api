package com.api.formSync.exception;

public class TodayLimitReachedException extends RuntimeException {
    public TodayLimitReachedException(String message) {
        super(message);
    }
}
