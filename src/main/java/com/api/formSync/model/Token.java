package com.api.formSync.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class Token {
    private String token;
    private Instant expireAt;

    public Token(String token) {
        this.token = token;
        this.expireAt = Instant.now().plusSeconds(15 * 60);
    }
}