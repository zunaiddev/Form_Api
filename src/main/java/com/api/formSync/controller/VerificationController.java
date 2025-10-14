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

        if (purpose.equals(Purpose.RESET_PASSWORD) && req == null) {
            throw new RequestBodyIsMissingException("Please Provide new Password.");
        }

        return switch (purpose) {
            case Purpose.VERIFY_USER -> SuccessRes.build(service.verifyUser(email, token, res));
            case Purpose.RESET_PASSWORD -> SuccessRes.build(service.resetPassword(email, req.getPassword(), token));
            case Purpose.UPDATE_EMAIL -> SuccessRes.build(service.updateEmail(email,
                    http.getAttribute("newEmail").toString(), token));

            default -> throw new InvalidTokenException("Invalid Token Type.");
        };
    }
}