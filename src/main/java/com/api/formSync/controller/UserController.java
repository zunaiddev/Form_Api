package com.api.formSync.controller;

import com.api.formSync.dto.FormResponse;
import com.api.formSync.dto.UserResponse;
import com.api.formSync.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable @NotNull Long id, HttpServletRequest http) {
        return ResponseEntity.ok(service.getUser(id, http));
    }

    @GetMapping("{id}/forms")
    public ResponseEntity<List<FormResponse>> getForms(@PathVariable @NotNull Long id, HttpServletRequest http) {
        return ResponseEntity.ok(service.getForms(id, http));
    }
}
