package com.api.formSync.controller;

import com.api.formSync.dto.LoginRequest;
import com.api.formSync.dto.LoginResponse;
import com.api.formSync.dto.SignupRequest;
import com.api.formSync.dto.SignupResponse;
import com.api.formSync.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<Map<String, String>> verify(@RequestParam @NotBlank String token) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.verify(token));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        return ResponseEntity.ok(service.authenticate(req, response));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        LoginResponse response = service.refreshToken(refreshToken);

        if (response == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or Expired Refresh Token");
        }

        return ResponseEntity.ok(response);
    }
}