package com.api.formSync.controller;

import com.api.formSync.Service.UserService;
import com.api.formSync.dto.PasswordRequest;
import com.api.formSync.dto.Response;
import com.api.formSync.dto.UserDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/info")
    public ResponseEntity<Response> getUser(Authentication auth) {
        return ResponseEntity.ok(Response.build(HttpStatus.OK, service.getUser(auth)));
    }

    @GetMapping("/forms")
    public ResponseEntity<Response> getForms(Authentication auth) {
        return ResponseEntity.ok(Response.build(HttpStatus.OK, service.getForms(auth)));
    }

    @PutMapping("/info/{id}")
    public ResponseEntity<Response> updateUser(Authentication auth, @PathVariable @NotNull Long id, @RequestBody @NotNull Map<String, Object> updates) {
        return ResponseEntity.ok(Response.build(HttpStatus.OK, new UserDTO(service.update(auth, id, updates))));
    }

    @DeleteMapping("/forms/{id}")
    public ResponseEntity<Response> test(@PathVariable Long id, Authentication auth) {
        service.deleteForm(id, auth);
        return ResponseEntity.ok(Response.build(HttpStatus.OK, "Form Deleted Successfully."));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response> deleteUser(Authentication auth, @PathVariable @NotNull Long id, @RequestBody @Valid PasswordRequest req, HttpServletResponse response) {
        return ResponseEntity.ok(Response.build(HttpStatus.OK, service.markAsDeleted(auth, id, req.getPassword(), response)));
    }

    @GetMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletResponse response) {
        return ResponseEntity.ok(Response.build(HttpStatus.OK, service.logout(response)));
    }
}