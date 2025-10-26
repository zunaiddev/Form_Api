package com.api.formSync.util;

import lombok.Getter;

@Getter
public class VerificationToken extends TokenData {
    private final String newEmail;

    public VerificationToken(String email, String newEmail, Role role, Purpose purpose) {
        super(email, role, purpose);
        this.newEmail = newEmail;
    }
}
