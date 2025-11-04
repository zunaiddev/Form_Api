package com.api.formSync.Component;

import com.api.formSync.model.UsedToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TokenCache {
    private final Set<UsedToken> tokens;

    public TokenCache() {
        this.tokens = new HashSet<>();
    }

    public void save(UsedToken token) {
        tokens.add(token);
    }

    public void delete(String token) {
        UsedToken savedToken = tokens
                .stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst().orElse(null);

        tokens.remove(savedToken);
    }

    public UsedToken get(String token) {
        return tokens
                .stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst().orElse(null);
    }

    public Set<UsedToken> getAll() {
        return this.tokens;
    }

    public boolean exists(String token) {
        return tokens
                .stream()
                .anyMatch(t -> t.getToken().equals(token));
    }

    public void deleteExpired() {
        List<UsedToken> tokens = this.tokens.stream().
                filter(token -> Instant.now().isBefore(Instant.now()))
                .toList();

        tokens.forEach(this.tokens::remove);
    }
}