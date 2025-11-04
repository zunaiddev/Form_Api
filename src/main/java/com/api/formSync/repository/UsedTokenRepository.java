package com.api.formSync.repository;

import com.api.formSync.model.UsedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsedTokenRepository extends JpaRepository<UsedToken, Long> {
    Optional<UsedToken> findByToken(String token);

    boolean existsByToken(String token);
}