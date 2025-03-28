package com.api.formSync.controller;

import com.api.formSync.Service.UserService;
import com.api.formSync.dto.PasswordRequest;
import com.api.formSync.dto.SuccessResponse;
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
    public ResponseEntity<SuccessResponse> getUser(Authentication auth) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Success", service.getUser(auth)));
    }

    @GetMapping("/forms")
    public ResponseEntity<SuccessResponse> getForms(Authentication auth) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Success", service.getForms(auth)));
    }

    @PutMapping("/info/{id}")
    public ResponseEntity<SuccessResponse> updateUser(Authentication auth, @PathVariable @NotNull Long id, @RequestBody @NotNull Map<String, Object> updates) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Success", new UserDTO(service.update(auth, id, updates))));
    }

    @DeleteMapping("/forms/{id}")
    public ResponseEntity<SuccessResponse> test(@PathVariable Long id, Authentication auth) {
        service.deleteForm(id, auth);
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Success", "Form Deleted Successfully."));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<SuccessResponse> deleteUser(Authentication auth, @PathVariable @NotNull Long id, @RequestBody @Valid PasswordRequest req, HttpServletResponse response) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Success", service.markAsDeleted(auth, id, req.getPassword(), response)));
    }

    @GetMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(HttpServletResponse response) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Success", service.logout(response)));
    }
}