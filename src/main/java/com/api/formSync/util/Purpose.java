package com.api.formSync.util;

public enum Purpose {
    AUTHENTICATION,
    RESET_PASSWORD,
    VERIFY_USER,
    REFRESH_TOKEN,
    UPDATE_EMAIL,
    REACTIVATE_USER;

    public static Purpose from(Object object) {
        Purpose purpose;

        try {
            purpose = valueOf(object.toString());
        } catch (Exception _) {
            purpose = null;
        }

        return purpose;
    }
}