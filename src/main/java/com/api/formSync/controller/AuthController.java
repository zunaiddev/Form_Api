package com.api.formSync.controller;

import com.api.formSync.Service.AuthService;
import com.api.formSync.dto.EmailRequest;
import com.api.formSync.dto.LoginRequest;
import com.api.formSync.dto.SignupRequest;
import com.api.formSync.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> register(@Valid @RequestBody SignupRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.build(HttpStatus.CREATED, "User created Successfully.", service.register(req)));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        return ResponseEntity
                .ok(SuccessResponse.build(HttpStatus.OK, "Authentication Success.", service.authenticate(req, response)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        return ResponseEntity
                .ok(SuccessResponse.build(HttpStatus.OK, "Token Refreshed Successfully.", service.refreshToken(refreshToken)));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<SuccessResponse> forgetPassword(@RequestBody @Valid EmailRequest req) {
        return ResponseEntity
                .ok(SuccessResponse.build(HttpStatus.OK, "Password Reset Email Sent.", service.resetPassword(req.getEmail())));
    }
}