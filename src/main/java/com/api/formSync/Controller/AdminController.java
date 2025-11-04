package com.api.formSync.Controller;

import com.api.formSync.Dto.UserInfo;
import com.api.formSync.Service.AdminService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private AdminService service;

    @GetMapping("/users")
    public ResponseEntity<List<UserInfo>> getAll() {
        System.out.println("request reswed ");
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

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable @NotNull Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserInfo> updateUser(@PathVariable @NotNull Long id, @RequestBody @NotNull Map<String, Object> updates) {
        return ResponseEntity.ok(service.updateUser(id, updates));
    }
}
