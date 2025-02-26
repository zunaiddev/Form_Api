package com.api.formSync.controller;

import com.api.formSync.dto.*;
import com.api.formSync.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    public ResponseEntity<SignupResponse> register(@Valid @RequestBody SignupRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(req));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam @NotBlank String token) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.verify(token));
    }

    @PostMapping("/resend-token")
    public ResponseEntity<ResendTokenResponse> resendToken(@Valid @RequestBody EmailRequest req) {
        return ResponseEntity.ok(service.resendToken(req.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.authenticate(req));
    }
}