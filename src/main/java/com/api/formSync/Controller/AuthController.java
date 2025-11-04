package com.api.formSync.Controller;

import com.api.formSync.Dto.*;
import com.api.formSync.Service.AuthService;
import com.api.formSync.util.Common;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignupResponse signup(@Valid @RequestBody SignupRequest req) {
        return service.signup(req);
    }

    @PostMapping("/login")
    public SignInResponse signIn(@Valid @RequestBody LoginRequest req,
                                             HttpServletResponse response) {
        return service.signIn(req, response);
    }

    @GetMapping("/refresh")
    public SignInResponse refreshToken(@CookieValue("refresh_token") String refreshToken) {
        return service.refreshToken(refreshToken);
    }

    @PostMapping("/forget-password")
    public EmailResponse forgetPassword(@RequestBody @Valid EmailRequest req) {
        return service.forgetPassword(req.getEmail());
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        response.addCookie(Common.getEmptyCookie());
    }
}