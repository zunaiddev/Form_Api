package com.api.formSync.repository;

import com.api.formSync.model.ApiKey;
import com.api.formSync.model.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiKeyRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Test
    @Order(1)
    void saveUser() {
        var user = new User("John Doe", "john@gmail.com", "John@123");
        user = userRepository.save(user);
        user.setKey(new ApiKey(user));
        userRepository.save(user);
    }

    @Test
    @Order(2)
    void findByUserId() {
        var apiKey = apiKeyRepository.findByUserId(1L)
                .orElse(null);
        assertNotNull(apiKey);
    }
}