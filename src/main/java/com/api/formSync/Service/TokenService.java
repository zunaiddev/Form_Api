package com.api.formSync.Service;

import com.api.formSync.Component.TokenCache;
import com.api.formSync.model.UsedToken;
import com.api.formSync.repository.UsedTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
    private final TokenCache cache;
    private final UsedTokenRepository repo;

    public void saveToken(String token) {
        UsedToken usedToken = new UsedToken(token);
        cache.save(repo.save(usedToken));
    }

    public void saveToken(UsedToken usedToken) {
        cache.save(repo.save(usedToken));
    }

    public boolean isTokenUsed(String token) {
        return cache.exists(token) || repo.existsByToken(token);
    }
}