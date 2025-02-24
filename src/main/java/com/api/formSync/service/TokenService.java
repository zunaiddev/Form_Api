package com.api.formSync.service;

import com.api.formSync.exception.InvalidTokenException;
import com.api.formSync.model.Token;
import com.api.formSync.model.User;
import com.api.formSync.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository repo;

    public String generateToken(User user) {
        return repo.save(new Token(user)).getToken();
    }

    public String regenerateToken(User user) {
        Token token = repo.findByUser(user);
        token.regenerate();
        repo.save(token);
        return token.getToken();
    }

    public void verify(String token, Long id) {
        Token savedToken = repo.findByToken(token);

        if (savedToken == null || savedToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token expired or Invalid");
        }

        if (!Objects.equals(savedToken.getUser().getId(), id)) {
            throw new InvalidTokenException("Invalid id");
        }

        repo.delete(savedToken);
    }
}
