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
    public SuccessRes<SignupResponse> register(@Valid @RequestBody SignupRequest req) {
        return SuccessRes.build(HttpStatus.CREATED, service.register(req));
    }

    @PostMapping("/login")
    public SuccessRes<LoginResponse> login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        return SuccessRes.build(service.authenticate(req, response));
    }

    @PostMapping("/refresh")
    public SuccessRes<LoginResponse> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        return SuccessRes.build(service.refreshToken(refreshToken));
    }

    @PostMapping("/forget-password")
    public SuccessRes<String> forgetPassword(@RequestBody @Valid EmailRequest req) {
        return SuccessRes.build(service.resetPassword(req.getEmail()));
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        service.logout(response);
    }
}