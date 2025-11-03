package com.api.formSync.Service;

import com.api.formSync.Component.TokenCache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
    private final TokenCache cache;

    public void saveToken(String token) {
        cache.save(token);
    }

    public boolean isTokenUsed(String token) {
        return cache.exists(token);
    }
}
