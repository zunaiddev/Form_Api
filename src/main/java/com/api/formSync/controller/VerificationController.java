package com.api.formSync.controller;

import com.api.formSync.Service.VerificationService;
import com.api.formSync.dto.ResetPasswordRequest;
import com.api.formSync.dto.Response;
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
    private ResponseEntity<Response> verify(HttpServletRequest http, @PathParam("token") String token,
                                            @RequestBody(required = false) @Valid ResetPasswordRequest req) {
        String purpose = http.getAttribute("purpose").toString();
        String email = http.getAttribute("email").toString();
        System.out.println(req);

        if (purpose.equals("reset_password") && req == null) {
            return new ResponseEntity<>(Response.build(HttpStatus.BAD_REQUEST, "new password is Missing"), HttpStatus.BAD_REQUEST);
        }

        return switch (purpose) {
            case "verify_user" -> ResponseEntity.ok(Response.build(HttpStatus.OK, service.verifyUser(email, token)));
            case "reset_password" ->
                    ResponseEntity.ok(Response.build(HttpStatus.OK, service.resetPassword(email, req.getPassword(), token)));
            case "update_email" ->
                    ResponseEntity.ok(Response.build(HttpStatus.OK, service.updateEmail(email, http.getAttribute("newEmail").toString(), token)));
            default ->
                    new ResponseEntity<>(Response.build(HttpStatus.BAD_REQUEST, "Something Went Wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }
}
