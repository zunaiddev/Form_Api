package com.api.formSync.controller;

import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.Service.VerificationService;
import com.api.formSync.dto.ResetPasswordRequest;
import com.api.formSync.dto.VerificationResponse;
import com.api.formSync.util.VerificationToken;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService service;

    @PatchMapping
    private VerificationResponse<?> verify(@AuthenticationPrincipal UserPrincipal details,
                                                       @RequestAttribute VerificationToken claims,
                                                       @RequestBody(required = false) @Valid ResetPasswordRequest req,
                                                       HttpServletResponse res) {
        return service.verify(details.getUser(), claims, req, res);
    }
}