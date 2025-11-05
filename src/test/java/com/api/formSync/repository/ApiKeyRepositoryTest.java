package com.api.formSync.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiKeyRepositoryTest {
   /* @Autowired
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
    }*/
}