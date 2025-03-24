package com.api.formSync.controller;

import com.api.formSync.dto.FormResponse;
import com.api.formSync.dto.UserInfo;
import com.api.formSync.model.User;
import com.api.formSync.service.AdminService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private AdminService service;

    @GetMapping("/users")
    public ResponseEntity<List<UserInfo>> getAll() {
        return ResponseEntity.ok(service.getUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserInfo> get(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(service.getUser(id));
    }

    @GetMapping("/users2")
    public ResponseEntity<UserInfo> get(@PathParam("email") @NotBlank String email) {
        return ResponseEntity.ok(service.getUser(email));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> update(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/forms/{id}")
    public ResponseEntity<List<FormResponse>> formsByUserId(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(service.getFormsByUserId(id));
    }

    @GetMapping("/forms")
    public ResponseEntity<List<FormResponse>> form() {
        return ResponseEntity.ok(service.getForms());
    }

    @GetMapping("/forms/{id}")
    public ResponseEntity<FormResponse> form(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(service.getForm(id));
    }

    @DeleteMapping("/forms/{id}")
    public ResponseEntity<?> deleteForm(@PathVariable @NotNull Long id) {
        service.deleteForm(id);
        return ResponseEntity.noContent().build();
    }
}
