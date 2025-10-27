package com.api.formSync.controller;

import com.api.formSync.Service.AuthService;
import com.api.formSync.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessRes<SignupResponse> signup(@Valid @RequestBody SignupRequest req) {
        return SuccessRes.build(HttpStatus.CREATED, service.signup(req));
    }

    @PostMapping("/login")
    public SuccessRes<SignInResponse> signIn(@Valid @RequestBody LoginRequest req,
                                             HttpServletResponse response) {
        return SuccessRes.build(service.signIn(req, response));
    }

    @PostMapping("/refresh")
    public SuccessRes<SignInResponse> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        return SuccessRes.build(service.refreshToken(refreshToken));
    }

    @PostMapping("/forget-password")
    public SuccessRes<EmailResponse> forgetPassword(@RequestBody @Valid EmailRequest req) {
        return SuccessRes.build(service.forgetPassword(req.getEmail()));
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        service.logout(response);
    }
}