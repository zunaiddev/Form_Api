package com.api.formSync.Service;

import com.api.formSync.model.UsedToken;
import com.api.formSync.repository.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenService {
    private final TokenRepository repo;

    public void saveUsedToken(String token) {
        repo.save(new UsedToken(token));
    }

    public boolean isTokenUsed(String token) {
        return repo.findByToken(token).isPresent();
    }
}
