package com.api.formSync.controller;

import com.api.formSync.Service.VerificationService;
import com.api.formSync.dto.ResetPasswordRequest;
import com.api.formSync.dto.SuccessRes;
import com.api.formSync.exception.InvalidTokenException;
import com.api.formSync.exception.RequestBodyIsMissingException;
import com.api.formSync.util.Purpose;
import jakarta.servlet.http.HttpServletRequest;
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
    private SuccessRes<?> verify(HttpServletRequest http,
                                 @RequestHeader("Authorization") String token,
                                 @RequestBody(required = false) @Valid ResetPasswordRequest req, HttpServletResponse res) {
        Purpose purpose = Purpose.valueOf(http.getAttribute("purpose").toString());
        String email = http.getAttribute("email").toString();

        if (purpose.equals(Purpose.reset_password) && req == null) {
            throw new RequestBodyIsMissingException("Please Provide new Password.");
        }

        return switch (purpose) {
            case Purpose.verify_user -> SuccessRes.build(service.verifyUser(email, token, res));
            case Purpose.reset_password -> SuccessRes.build(service.resetPassword(email, req.getPassword(), token));
            case Purpose.update_email -> SuccessRes.build(service.updateEmail(email,
                    http.getAttribute("newEmail").toString(), token));

            default -> throw new InvalidTokenException("Invalid Token Type.");
        };
    }
}