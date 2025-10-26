package com.api.formSync.controller;

import com.api.formSync.Service.VerificationService;
import com.api.formSync.dto.ResetPasswordRequest;
import com.api.formSync.dto.SuccessRes;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService service;

    @GetMapping
    private SuccessRes<Object> verify(@RequestAttribute String newEmail,
                                      @RequestBody(required = false) @Valid ResetPasswordRequest req,
                                      HttpServletResponse res) {
        return SuccessRes.build(service.verify(newEmail, req, res));
    }
}