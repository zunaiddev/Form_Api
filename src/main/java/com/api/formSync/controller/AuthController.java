package com.api.formSync.controller;

import com.api.formSync.Service.AuthService;
import com.api.formSync.dto.*;
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
    public ResponseEntity<Response> register(@Valid @RequestBody SignupRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.build(HttpStatus.CREATED, service.register(req)));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        return ResponseEntity.ok(Response.build(HttpStatus.OK, service.authenticate(req, response)));
    }

    @GetMapping("/available")
    public ResponseEntity<Response> isAvailable(@RequestBody @Valid EmailRequest req) {
        return ResponseEntity.ok(Response.build(HttpStatus.OK, service.isAvailable(req.getEmail())));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Response> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        LoginResponse response = service.refreshToken(refreshToken);

        if (response == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or Expired Refresh Token");
        }

        return ResponseEntity.ok(Response.build(HttpStatus.OK, response));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<Response> forgetPassword(@RequestBody @Valid EmailRequest req) {
        return ResponseEntity.ok(Response.build(HttpStatus.OK, service.resetPassword(req.getEmail())));
    }
}