package com.api.formSync.controller;

import com.api.formSync.dto.FormResponse;
import com.api.formSync.dto.UpdateEmailRequest;
import com.api.formSync.dto.UserInfo;
import com.api.formSync.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/info")
    public ResponseEntity<UserInfo> getUser(Authentication auth) {
        return ResponseEntity.ok(service.getUser(auth));
    }

    @GetMapping("/forms")
    public ResponseEntity<List<FormResponse>> getForms(Authentication auth) {
        return ResponseEntity.ok(service.getForms(auth));
    }

    @PutMapping("/update-email")
    public ResponseEntity<UserInfo> updateEmail(@RequestBody @Valid UpdateEmailRequest req, Authentication auth) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-password")
    public ResponseEntity<UserInfo> updateEmail() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/forms/{id}")
    public ResponseEntity<?> test(@PathVariable Long id, Authentication auth) {
        service.deleteForm(id, auth);
        return ResponseEntity.noContent().build();
    }
}