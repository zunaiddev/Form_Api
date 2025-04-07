package com.api.formSync.controller;

import com.api.formSync.Principal.ApiKeyPrincipal;
import com.api.formSync.Service.FormService;
import com.api.formSync.dto.FormRequest;
import com.api.formSync.dto.FormResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@AllArgsConstructor
public class FormController {
    private final FormService service;

    @PostMapping("/submit")
    public ResponseEntity<FormResponse> submit(@AuthenticationPrincipal ApiKeyPrincipal details, @Valid @RequestBody FormRequest req) {
        return ResponseEntity.ok(service.submit(req, details));
    }
}
