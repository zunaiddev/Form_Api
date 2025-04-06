package com.api.formSync.controller;

import com.api.formSync.Service.VerificationService;
import com.api.formSync.dto.ResetPasswordRequest;
import com.api.formSync.dto.SuccessResponse;
import com.api.formSync.exception.InvalidTokenException;
import com.api.formSync.exception.RequestBodyIsMissingException;
import com.api.formSync.util.Purpose;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService service;

    @GetMapping
    private ResponseEntity<SuccessResponse> verify(HttpServletRequest http, @RequestHeader("Authorization") String token,
                                                   @RequestBody(required = false) @Valid ResetPasswordRequest req, HttpServletResponse res) {
        Purpose purpose = Purpose.valueOf(http.getAttribute("purpose").toString());
        String email = http.getAttribute("email").toString();

        if (purpose.equals(Purpose.reset_password) && req == null) {
            throw new RequestBodyIsMissingException("Please Provide new Password.");
        }

        return switch (purpose) {
            case Purpose.verify_user ->
                    ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "User Verified Successfully", service.verifyUser(email, token, res)));
            case Purpose.reset_password ->
                    ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Password Reset Successfully", service.resetPassword(email, req.getPassword(), token)));
            case Purpose.update_email ->
                    ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Email Updated", service.updateEmail(email, http.getAttribute("newEmail").toString(), token)));
            default -> throw new InvalidTokenException("Invalid Token Type.");
        };
    }
}