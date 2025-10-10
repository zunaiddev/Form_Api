package com.api.formSync.Service;

import com.api.formSync.model.Domain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DomainServiceTest {
    @Autowired
    private DomainService domainService;

    @Autowired
    private UserInfoService userService;

    @Autowired
    private ApiKeyService apiKeyService;

    @Test
    void createUsers() {
        userService.create("John", "john@gmail.com", "john@123");
        userService.create("John", "john@gmail.com", "john@123");
        System.out.println("User created");
    }

    @Test
    void create() {
        Domain domain = new Domain(1L, "www.google.com1", null);
        Domain domain2 = new Domain(1L, "www.google.com2", null);
        Domain domain3 = new Domain(1L, "www.google.com3", null);
        Domain domain4 = new Domain(1L, "www.google.com4", null);


    }

    @Test
    void delete() {
    }
}