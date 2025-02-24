package com.api.formSync.service;

import com.api.formSync.repository.TokenRepository;
import com.api.formSync.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CleanupService {
    private final UserRepository userRepo;
    private final TokenRepository tokenRepo;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanDatabase() {
        tokenRepo.deleteExpiredTokens(LocalDateTime.now());
        userRepo.deleteUnverifiedUsers(LocalDateTime.now().minusDays(7));
    }
}
