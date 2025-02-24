package com.api.formSync.repository;

import com.api.formSync.model.Token;
import com.api.formSync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);

    Token findByUser(User user);

    @Query("DELETE FROM Token t WHERE t.expiryDate < :now")
    void deleteExpiredTokens(LocalDateTime now);
}