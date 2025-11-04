package com.api.formSync.Exception;

public class TodayLimitReachedException extends RuntimeException {
    public TodayLimitReachedException(String message) {
        super(message);
    }
}