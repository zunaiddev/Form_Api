package com.api.formSync.util;

public enum Purpose {
    AUTHENTICATION,
    RESET_PASSWORD,
    VERIFY_USER,
    REFRESH_TOKEN,
    UPDATE_EMAIL,
    REACTIVATE;

    public static Purpose from(Object object) {
        try {
            return valueOf(object.toString());
        } catch (Exception _) {
            return null;
        }
    }
}