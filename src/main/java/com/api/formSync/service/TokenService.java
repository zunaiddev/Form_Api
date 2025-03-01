package com.api.formSync.service;

import com.api.formSync.exception.InvalidTokenException;
import com.api.formSync.exception.TokenExpiredException;
import com.api.formSync.model.TempUser;
import com.api.formSync.model.Token;
import com.api.formSync.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository repo;

    public String generateToken(TempUser user) {
        Token token = repo.findByUser(user).orElse(null);

        if (token == null) {
            return repo.save(new Token(user)).getToken();
        }

        token.regenerate();
        return repo.save(token).getToken();
    }

    public TempUser verify(String token) {
        Token savedToken = repo.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid Token"));

        if (savedToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token has expired. Please Signup again");
        }

        repo.delete(savedToken);

        return savedToken.getUser();
    }
}
