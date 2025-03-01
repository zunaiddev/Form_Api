package com.api.formSync.controller;

import com.api.formSync.dto.FormRequest;
import com.api.formSync.dto.FormResponse;
import com.api.formSync.service.FormService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<FormResponse> submit(@Valid @RequestBody FormRequest req, HttpServletRequest http) {
        return ResponseEntity.ok(service.submit(req, http));
    }

    @PostMapping("/test")
    public String test(@Valid @RequestBody FormRequest req, HttpServletRequest http) {
        return "no error";
    }
}
