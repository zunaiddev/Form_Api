package com.api.formSync.model;

import com.api.formSync.Component.TokenCache;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TokenCacheTest {
    @Autowired
    private TokenCache tokenCache;
    @Autowired
    private TokenCache tokenCache2;
    @Autowired
    private TokenCache tokenCache3;

    @Test
    @Order(1)
    void save() {
        tokenCache.save("token1");
        tokenCache.save("token2");
        tokenCache.save("token3");
        tokenCache.save("token4");
    }

    @Test
    @Order(2)
    void delete() {
        tokenCache3.delete("token2");
    }

    @Test
    @Order(3)
    void getAll() {
        System.out.println(tokenCache2.getAll());
    }
}