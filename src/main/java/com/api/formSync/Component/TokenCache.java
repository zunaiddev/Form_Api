package com.api.formSync.Component;

import com.api.formSync.model.Token;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TokenCache {
    private final Set<Token> tokenSet;

    public TokenCache() {
        this.tokenSet = new HashSet<>();
    }

    public void save(String token) {
        tokenSet.add(new Token(token));
    }

    public void delete(String token) {
        Token savedToken = tokenSet
                .stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst().orElse(null);

        tokenSet.remove(savedToken);
    }

    public Token get(String token) {
        return tokenSet
                .stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst().orElse(null);
    }

    public Set<Token> getAll() {
        return this.tokenSet;
    }

    public boolean exists(String token) {
        return tokenSet
                .stream()
                .anyMatch(t -> t.getToken().equals(token));
    }

    public void deleteExpired() {
        List<Token> tokens = tokenSet.stream().
                filter(token -> Instant.now().isBefore(Instant.now()))
                .toList();

        tokens.forEach(tokenSet::remove);
    }
}