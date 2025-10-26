package com.api.formSync.Service;

import com.api.formSync.dto.LoginRequest;
import com.api.formSync.dto.SignupRequest;
import com.api.formSync.exception.UnverifiedEmailException;
import com.api.formSync.model.User;
import com.api.formSync.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.LockedException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest {
    static SignupRequest signupRequest;
    static LoginRequest loginRequest;
    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepo;
    @Mock
    MockHttpServletResponse response;
    static String token;
    static User user;
    @Autowired
    GenerateTokenService generateTokenService;

    @BeforeAll
    static void setUp() {
        signupRequest = new SignupRequest("John", "john@gmail.com", "john@123");
        loginRequest = new LoginRequest("john@gmail.com", "john@123");
    }

    @Test
    @Order(1)
    void signup() {
        var res = authService.signup(signupRequest);
        user = userRepo.findByEmail("john@gmail.com").get();
        user.setEnabled(true);
        user.setLocked(true);
//        user.setDeleteAt(LocalDateTime.now().plusDays(3L));
        userRepo.save(user);

        System.out.println("User Created Successfully");
        System.out.println("Response: " + res);
    }

    @Test
    @Order(2)
    @Disabled
    void signIn() {
        var res = authService.signIn(loginRequest, response);
        System.out.println("SignIn res: " + res);
    }

    @Test
    @Order(3)
    @Disabled
    void forgetPassword() {
        assertThrowsExactly(UnverifiedEmailException.class, () -> {
            authService.forgetPassword("john@gmail.com");
        });
    }

    @Test
    @Order(4)
    void refreshToken() {
        var token = generateTokenService.refreshToken(user);
        assertThrowsExactly(LockedException.class, () -> {
            authService.refreshToken(token);
        });
//        var res = authService.refreshToken(token);
//        assertEquals(UserStatus.ACTIVE, res.getStatus());
//        assertNotNull(res.getToken());
    }
}