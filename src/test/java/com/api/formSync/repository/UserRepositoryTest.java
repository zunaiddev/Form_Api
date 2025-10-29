package com.api.formSync.repository;

import com.api.formSync.model.ApiKey;
import com.api.formSync.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {
    private static User user;
    @Autowired
    UserRepository userRepo;
    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Test
    @Order(1)
    public void save() {
        User unsaved = new User("Alice", "alice@gmail.com", "Alice@123");
        user = userRepo.save(unsaved);

        ApiKey apiKey = apiKeyRepository.save(new ApiKey(user));
        user.setKey(apiKey);
        user = userRepo.save(user);
    }

    @Test
    @Order(2)
    @Transactional
    public void findWithKeyById() {
        User fetchedUser = userRepo.findWithKeyById(user.getId()).orElse(null);
//        System.out.println("Fetched User with Key: " + fetchedUser);
        Assertions.assertNotNull(fetchedUser);
        System.out.println("Fetched User Key: " + fetchedUser.getKey());
    }
}