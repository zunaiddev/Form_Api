package com.api.formSync.Service;

import com.api.formSync.dto.ApiKeyInfo;
import com.api.formSync.model.User;
import com.api.formSync.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
    void regenerateKey() {
        ApiKeyInfo info = userService.regenerateKey(user.getId());
        System.out.println("Regenerated Info " + info);
    }
}