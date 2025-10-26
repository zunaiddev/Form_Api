package com.api.formSync.Service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtServiceTest {
    static String token;
    static JwtService jwtService = new JwtService();

    @BeforeAll
    static void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", "hjfdevdeghdedefdeghdedfgedfhgcedevdghedecdfghedcfhecdedcfedcfedcfhed vedcevdceghdcehdcfghecdedcfgedfgedcefd");
        ReflectionTestUtils.setField(jwtService, "environment", "test-env");
    }

    @Test
    @Order(1)
    void generateToken() throws InterruptedException {
        token = jwtService.generateToken(1L, Map.of(), 1);
        System.out.println("Sleeping...");
        Thread.sleep(1200);
        System.out.println("Generated Token: " + token);
    }

    @Test
    @Order(2)
    @Disabled
    void isTokenExpired() {
        boolean expired = jwtService.isTokenExpired(token);
        System.out.println("Token Status: " + expired);
        assertFalse(expired);
    }

    @Test
    @Order(3)
    @Disabled
    void extractSubject() {
        Object obj = jwtService.extractSubject(token);
        System.out.println("Subject: " + obj);
    }

    @Test
    @Order(4)
    void extractClaims() {
        Claims claims = jwtService.extractClaims(token);
        System.out.println("claims: " + claims);
    }
}