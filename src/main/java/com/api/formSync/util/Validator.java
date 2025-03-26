package com.api.formSync.util;

import java.util.regex.Pattern;

public class Validator {
    public static boolean name(String name) {
        return Pattern.matches("^[a-zA-Z\\s'-]+$", name);
    }

    public static boolean email(String email) {
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email);
    }

    public static boolean password(String password) {
        return Pattern.matches("^(?!.*\\\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$&*]).{8,20}$", password);
    }

    public static boolean isBool(Object obj) {
        return obj.getClass().equals(Boolean.class);
    }

    public static boolean isRole(String role) {
        return role.equalsIgnoreCase(Role.ADMIN.name())
                || role.equalsIgnoreCase(Role.USER.name())
                || role.equalsIgnoreCase(Role.ULTIMATE.name());
    }
}
