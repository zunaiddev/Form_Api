package com.api.formSync.Controller;

import com.api.formSync.Dto.*;
import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public UserInfo info(@AuthenticationPrincipal UserPrincipal details) {
        return service.userInfo(details.getId());
    }

    @PatchMapping
    public UserInfo update(@AuthenticationPrincipal UserPrincipal details,
                                       @RequestBody @Valid UserUpdateRequest req) {
        return service.updateUser(details.getId(), req);
    }

    @PatchMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserPrincipal details,
                                             @RequestBody @Valid ChangePasswordRequest req) {
        service.changePassword(details.getId(), req);
        return "Password Changed Successfully";
    }

    @PatchMapping("/change-email")
    public EmailResponse changeEmail(@AuthenticationPrincipal UserPrincipal details,
                                          @RequestBody @Valid ChangeEmailRequest req) {
        return service.changeEmail(details.getId(), req);
    }

    @DeleteMapping
    public SuccessRes<String> deleteUser(@AuthenticationPrincipal UserPrincipal details,
                                         @RequestBody @Valid PasswordRequest req,
                                         HttpServletResponse res) {
        service.deleteUser(details.getId(), req, res);
        return SuccessRes.build("User Deletion Initiated. Your account will be permanently deleted after 3 days.");
    }

    @GetMapping("/api-key")
    public ApiKeyInfo keyInfo(@AuthenticationPrincipal UserPrincipal details) {
        return service.getKeyInfo(details.getId());
    }

    @PostMapping("/api-key")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiKeyInfo generate(@AuthenticationPrincipal UserPrincipal details) {
        return service.generateKey(details.getId());
    }

    @PatchMapping("/api-key/regenerate")
    public ApiKeyInfo regenerate(@AuthenticationPrincipal UserPrincipal details) {
        return service.regenerateKey(details.getId());
    }

    @PatchMapping("/api-key/status")
    public ApiKeyInfo activate(@AuthenticationPrincipal UserPrincipal details,
                                           @RequestBody @Valid ApiKeyStatusRequest req) {

        return service.updateStatus(details.getId(), req.getActive());
    }

    @PostMapping("/api-key/domain")
    public ApiKeyInfo addDomain(@AuthenticationPrincipal UserPrincipal details,
                                            @RequestBody @Valid DomainRequest req) {
        return service.addDomain(details.getId(), req.getDomain());
    }

    @DeleteMapping("/api-key/domain/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDomain(@AuthenticationPrincipal UserPrincipal details,
                                          @NotNull(message = "id could not be null") @PathVariable("id") Long id) {
        service.removeDomain(details.getId(), id);
    }

    @GetMapping("/forms")
    public List<FormResponse> getForms(@AuthenticationPrincipal UserPrincipal details) {
        return service.getForms(details.getId());
    }

    @DeleteMapping("/forms")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteForm(@AuthenticationPrincipal UserPrincipal details,
                           @RequestBody Set<Long> ids) {
        service.deleteForms(details.getId(), ids);
    }
}