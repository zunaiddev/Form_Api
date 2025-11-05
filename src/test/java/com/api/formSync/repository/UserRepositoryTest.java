package com.api.formSync.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {
   /* private static User user;
    @Autowired
    UserRepository userRepo;
    @Autowired
    ApiKeyRepository apiKeyRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    UserService userService;

    @Test
    @Order(1)
    public void save() {
        User savedUser = userRepo.save(new User("Alice", "alice@gmail.com", "Alice@123"));
        ApiKey apiKey = apiKeyRepository.save(new ApiKey(savedUser));

        savedUser.setKey(apiKey);
        userRepo.save(savedUser);

        Set<Domain> domains = new HashSet<>();
        domains.add(new Domain("domain1.com"));
        domains.add(new Domain("domain2.com"));

        Set<Domain> savedDomains = new HashSet<>(domainRepository.saveAll(domains));
        apiKey.setDomains(savedDomains);

        apiKeyRepository.save(apiKey);
    }

    @Test
    @Order(2)
    @Disabled
    @Transactional
    public void findWithKeyById() {
        User foundUser = userRepo.findWithKeyById(1L).orElse(null);
    }*/
}