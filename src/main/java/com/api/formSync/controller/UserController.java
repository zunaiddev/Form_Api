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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public SuccessRes<UserInfo> info(@AuthenticationPrincipal UserPrincipal details) {
        return SuccessRes.build(service.userInfo(details.getId()));
    }

    @PatchMapping
    public SuccessRes<UserInfo> update(@AuthenticationPrincipal UserPrincipal details,
                                       @RequestBody @Valid UserUpdateRequest req) {
        return SuccessRes.build(service.updateUser(details.getId(), req));
    }

    @DeleteMapping
    public SuccessRes<String> deleteUser(@AuthenticationPrincipal UserPrincipal details, @RequestBody @Valid PasswordRequest req, HttpServletResponse res) {
        return SuccessRes.build("Currently Under Development");
    }

    @GetMapping("/api-key")
    public SuccessRes<ApiKeyInfo> keyInfo(@AuthenticationPrincipal UserPrincipal details) {
        return SuccessRes.build(service.getKeyInfo(details.getId()));
    }

    @PostMapping("/api-key")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessRes<ApiKeyInfo> generate(@AuthenticationPrincipal UserPrincipal details) {
        return SuccessRes.build(HttpStatus.CREATED, service.generateKey(details.getId()));
    }

    @PutMapping("/api-key")
    public SuccessRes<ApiKeyInfo> regenerate(@AuthenticationPrincipal UserPrincipal details) {
        return SuccessRes.build(service.regenerateKey(details.getId()));
    }

    @PostMapping("/api-key/domain")
    public SuccessRes<ApiKeyInfo> addDomain(@AuthenticationPrincipal UserPrincipal details,
                                            @RequestBody @Valid DomainRequest req) {
        return SuccessRes.build(service.addDomain(details.getId(),
                req.getDomain()));
    }

    @DeleteMapping("/api-key/domain/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDomain(@AuthenticationPrincipal UserPrincipal details,
                                          @NotNull(message = "id could not be null") @PathVariable("id") Long id) {
        service.deleteDomain(details.getId(), id);
    }

//    @DeleteMapping("/api-key")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteKey(@AuthenticationPrincipal UserPrincipal details) {
//        service.deleteApiKey(details.getUser());
//    }

    @GetMapping("/forms")
    public SuccessRes<List<FormResponse>> getForms(@AuthenticationPrincipal UserPrincipal details) {
        return SuccessRes.build(service.getForms(details.getId()));
    }

    @DeleteMapping("/forms")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteForm(@AuthenticationPrincipal UserPrincipal details,
                           @RequestBody List<Long> ids) {
        service.deleteForms(details.getId(), ids);
    }
}