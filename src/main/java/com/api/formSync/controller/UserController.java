package com.api.formSync.controller;

import com.api.formSync.dto.FormResponse;
import com.api.formSync.dto.UserInfo;
import com.api.formSync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/info")
    public ResponseEntity<UserInfo> getUser(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(service.getUser(auth));
    }

    @GetMapping("/forms")
    public ResponseEntity<List<FormResponse>> getForms(@RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(service.getForms(auth));
    }

    @DeleteMapping("/forms/{id}")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String auth) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
