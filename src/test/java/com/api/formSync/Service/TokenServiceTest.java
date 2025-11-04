package com.api.formSync.Service;

import com.api.formSync.model.UsedToken;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class TokenServiceTest {
    @Autowired
    private TokenService tokenService;

    @Test
    @Order(1)
    void saveToken() {
        UsedToken usedToken1 = new UsedToken("token1");
        UsedToken usedToken2 = new UsedToken("token2");

        tokenService.saveToken(usedToken1);
        tokenService.saveToken(usedToken2);
        System.out.println("Tokens saved successfully.");
    }

    @Test
    @Order(2)
    void isTokenUsed() {
        boolean r1 = tokenService.isTokenUsed("token1");
        boolean r2 = tokenService.isTokenUsed("token2");
        boolean r3 = tokenService.isTokenUsed("token3");

        System.out.println("Is token1 used? " + r1);
        System.out.println("Is token2 used? " + r2);
        System.out.println("Is token3 used? " + r3);
    }

    @Test
    @Order(3)
    void deleteExpiredTokens() {

    }
}