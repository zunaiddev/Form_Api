package com.api.formSync.controller;

import com.api.formSync.dto.LoginRequest;
import com.api.formSync.dto.SignupRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

@SpringBootTest
class AuthControllerTest {
    @Autowired
    private AuthController controller;

    @Mock
    private MockHttpServletResponse response;

    @Test
    void signup() {
        var req = new SignupRequest("John Doe", "john@gmail.com", "John@123");
        var response = controller.signup(req);
        System.out.println("Signup Response: " + response);
    }

    @Test
    void signIn() {
        var req = new LoginRequest("john@gmail.com", "John@123");
        var response = controller.signIn(req, this.response);
        System.out.println("SignIn Response: " + response);
    }

    @Test
    void refreshToken() {

    }

    @Test
    void forgetPassword() {
    }

    @Test
    void logout() {
    }
}