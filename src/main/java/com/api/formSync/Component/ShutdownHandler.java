package com.api.formSync.Component;

import com.api.formSync.model.Token;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class ShutdownHandler {
    private final TokenCache tokenCache;

    @PreDestroy
    public void handleShutdown() {
        tokenCache.deleteExpired();
        Set<Token> tokens = tokenCache.getAll();

        System.out.println("Application is shutting down. Performing cleanup...");
    }
}