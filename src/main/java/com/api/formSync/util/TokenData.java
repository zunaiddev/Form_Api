package com.api.formSync.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenData {
    protected String email;
    protected Role role;
    protected Purpose purpose;

    public TokenData(String email, Purpose purpose) {
        this.email = email;
        this.purpose = purpose;
    }
}