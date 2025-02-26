package com.api.formSync.service;

import com.api.formSync.exception.CooldownNotMetException;
import com.api.formSync.exception.InvalidTokenException;
import com.api.formSync.exception.TokenExpiredException;
import com.api.formSync.model.Token;
import com.api.formSync.model.User;
import com.api.formSync.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository repo;

    public String generateToken(User user) {
        return repo.save(new Token(user)).getToken();
    }

    public String regenerateToken(User user) {
        Token token = repo.findByUser(user);

        if (token.getCreatedAt().plusMinutes(2).isAfter(LocalDateTime.now())) {
            throw new CooldownNotMetException("Please wait 2 minutes before requesting another verification link");
        }

        token.regenerate();
        repo.save(token);
        return token.getToken();
    }

    public User verify(String token) {
        Token savedToken = repo.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid Token"));

        if (savedToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token has expired. Please resend the token");
        }

        repo.delete(savedToken);

        return savedToken.getUser();
    }
}
