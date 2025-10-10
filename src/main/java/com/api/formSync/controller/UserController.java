package com.api.formSync.controller;

import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.Service.UserService;
import com.api.formSync.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/info")
    public SuccessRes<UserInfo> info(@AuthenticationPrincipal UserPrincipal details) {
        return SuccessRes.build(service.getInfo(details));

    }

    @PutMapping("/info")
    public SuccessRes<UserInfo> update(@AuthenticationPrincipal UserPrincipal details, @RequestBody @Valid UserUpdateRequest req) {
        return SuccessRes.build(service.update(details, req));
    }

    @DeleteMapping
    public SuccessRes<String> deleteUser(@AuthenticationPrincipal UserPrincipal details, @RequestBody @Valid PasswordRequest req, HttpServletResponse res) {
        return SuccessRes.build(service.deleteUser(details, req, res));
    }

    @GetMapping("/key")
    public SuccessRes<ApiKeyInfo> keyInfo(@AuthenticationPrincipal UserPrincipal details) {
        return SuccessRes.build(service.getKeyInfo(details.getUser()));
    }

    @PostMapping("/key")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessRes<ApiKeyInfo> generate(@AuthenticationPrincipal UserPrincipal details,
                                           @RequestBody @Valid DomainRequest req) {
        return SuccessRes.build(HttpStatus.CREATED, service.generateKey(details.getUser(), req.getDomain()));
    }

    @PutMapping("/key")
    public SuccessRes<ApiKeyInfo> regenerate(@AuthenticationPrincipal UserPrincipal details) {
        return SuccessRes.build(service.regenerateKey(details.getUser()));
    }

    @PutMapping("/key/domain")
    public SuccessRes<ApiKeyInfo> addDomain(@AuthenticationPrincipal UserPrincipal details,
                                            @RequestBody @Valid DomainRequest req) {
        return SuccessRes.build(service.addDomain(details.getUser(),
                req.getDomain()));
    }

    @DeleteMapping("/key/domain/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDomain(@AuthenticationPrincipal UserPrincipal details,
                                          @NotNull(message = "id could not be null") @PathVariable("id") Long id) {
        service.deleteDomain(details.getUser(), id);
    }

    @DeleteMapping("/key")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteKey(@AuthenticationPrincipal UserPrincipal details) {
        service.deleteKey(details.getUser());
    }

    @GetMapping("/forms")
    public SuccessRes<List<FormResponse>> getForms(@AuthenticationPrincipal UserPrincipal details) {
        return SuccessRes.build(service.getForms(details.getUser()));
    }

    @DeleteMapping("/forms")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteForm(@AuthenticationPrincipal UserPrincipal details,
                           @Valid @RequestBody FormDeleteReq req) {
        service.deleteForms(details, req.getId());
    }
}