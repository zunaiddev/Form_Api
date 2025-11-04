package com.api.formSync.Service;

import com.api.formSync.Dto.ApiKeyInfo;
import com.api.formSync.model.User;
import com.api.formSync.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    private static User user;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    @Order(1)
    public void saveUser() {
        User unsaved = new User("Alice", "alice@gmail.com", "Alice@123");
        user = userRepository.save(unsaved);
    }

    @Test
    @Order(2)
    void generateKey() {
        ApiKeyInfo info = userService.generateKey(user.getId());
        System.out.println("Info " + info);
    }

    @Test
    @Order(3)
    @Disabled
    void regenerateKey() {
        ApiKeyInfo info = userService.regenerateKey(user.getId());
        System.out.println("Regenerated Info " + info);
    }

    @Test
    @Order(4)
    void addDomain() {
        userService.addDomain(1L, "www.example.com1");
        userService.addDomain(1L, "www.example.com2");
    }

    @Test
    @Order(5)
    void removeDomain() {
        userService.removeDomain(1L, 1L);
    }

    @Test
    @Order(6)
    void getDomains() {
        ApiKeyInfo apiKey = userService.getKeyInfo(1L);
        System.out.println("Api Key Domains: " + apiKey.getDomains());
    }
}