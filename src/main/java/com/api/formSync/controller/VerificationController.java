package com.api.formSync.controller;

import com.api.formSync.Service.VerificationService;
import com.api.formSync.dto.ResetPasswordRequest;
import com.api.formSync.dto.SuccessResponse;
import com.api.formSync.exception.RequestBodyIsMissingException;
import com.api.formSync.exception.SomethingWentWrongException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class VerificationController {
    private final VerificationService service;

    @PostMapping
    private ResponseEntity<SuccessResponse> verify(HttpServletRequest http, @PathParam("token") String token,
                                                   @RequestBody(required = false) @Valid ResetPasswordRequest req) {
        String purpose = http.getAttribute("purpose").toString();
        String email = http.getAttribute("email").toString();
        System.out.println(req);

        if (purpose.equals("reset_password") && req == null) {
            throw new RequestBodyIsMissingException("Please Provide new Password.");
        }

        return switch (purpose) {
            case "verify_user" ->
                    ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "User Verified Successfully", service.verifyUser(email, token)));
            case "reset_password" ->
                    ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Password Reset Successfully", service.resetPassword(email, req.getPassword(), token)));
            case "update_email" ->
                    ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Email Updated", service.updateEmail(email, http.getAttribute("newEmail").toString(), token)));
            default -> throw new SomethingWentWrongException("Something Went Wrong.");
        };
    }
}
