package com.api.formSync.controller;

import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.Service.UserService;
import com.api.formSync.dto.DomainRequest;
import com.api.formSync.dto.PasswordRequest;
import com.api.formSync.dto.SuccessResponse;
import com.api.formSync.dto.UserUpdateRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/info")
    public ResponseEntity<SuccessResponse> info(@AuthenticationPrincipal UserPrincipal details) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Success", service.getInfo(details)));

    }

    @PutMapping("/info")
    public ResponseEntity<SuccessResponse> update(@AuthenticationPrincipal UserPrincipal details, @RequestBody @Valid UserUpdateRequest req) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "success", service.update(details, req)));
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteUser(@AuthenticationPrincipal UserPrincipal details, @RequestBody @Valid PasswordRequest req, HttpServletResponse res) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Success", service.markAsDeleted(details, req, res)));
    }

    @GetMapping("/key")
    public ResponseEntity<SuccessResponse> keyInfo(@AuthenticationPrincipal UserPrincipal details) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "key info",
                service.getKeyInfo(details.getUser())));
    }

    @PostMapping("/key")
    public ResponseEntity<SuccessResponse> generate(@AuthenticationPrincipal UserPrincipal details,
                                                    @RequestBody @Valid DomainRequest req) {
        return new ResponseEntity<>(SuccessResponse.build(HttpStatus.CREATED, "api key generated",
                service.generateKey(details.getUser(), req.getDomain())), HttpStatus.CREATED);
    }

    @PutMapping("/key")
    public ResponseEntity<SuccessResponse> regenerate(@AuthenticationPrincipal UserPrincipal details) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "api key regenerated",
                service.regenerateKey(details.getUser())));
    }

    @PutMapping("/key/domain")
    public ResponseEntity<SuccessResponse> addDomain(@AuthenticationPrincipal UserPrincipal details,
                                                     @RequestBody @Valid DomainRequest req) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Domain Added Successfully",
                service.addDomain(details.getUser(), req.getDomain())));
    }

    @DeleteMapping("/key/domain")
    public ResponseEntity<SuccessResponse> deleteDomain(@AuthenticationPrincipal UserPrincipal details,
                                                        @RequestBody @Valid DomainRequest req) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Deleted",
                service.deleteDomain(details.getUser(), req.getDomain())));
    }

    @DeleteMapping("/key")
    public ResponseEntity<SuccessResponse> addDomain(@AuthenticationPrincipal UserPrincipal details) {
        return ResponseEntity.ok(SuccessResponse.build(HttpStatus.OK, "Deleted",
                service.deleteKey(details.getUser())));
    }
}