package com.api.formSync.repository;

import com.api.formSync.model.UsedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<UsedToken, Long> {
    Optional<UsedToken> findByToken(String token);
}
