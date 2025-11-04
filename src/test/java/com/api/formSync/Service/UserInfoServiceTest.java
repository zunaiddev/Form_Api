package com.api.formSync.Service;

import com.api.formSync.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserInfoServiceTest {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    PasswordEncoder encoder;


    @Test
    @Order(1)
    void save() {
        User user = new User("John", "john@gmail.com", encoder.encode("John@123"));
        user.setEnabled(false);

        userInfoService.save(user);
    }

    @Test
    @Order(2)
    @Disabled
    void getAuthentication() {
        Authentication auth = userInfoService.getAuthentication("john@gmail.com", "John@123");

        System.out.println("Authenticated " + auth.isAuthenticated());
    }
}