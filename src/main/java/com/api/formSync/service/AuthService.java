package com.api.formSync.service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Email.EmailTemplate;
import com.api.formSync.dto.*;
import com.api.formSync.exception.UserAlreadyVerifiedException;
import com.api.formSync.model.ApiKey;
import com.api.formSync.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final ApiKeyService apiKeyService;
    private final JwtService jwtService;

    public SignupResponse register(SignupRequest req) {
        User user = userService.create(req);
        sendEmail(user.getEmail(), user.getName(), tokenService.generateToken(user));
        return new SignupResponse("User Created Successfully. Please Verify Your Email", new UserDTO(user));
    }

    public String verify(String token) {
        User user = tokenService.verify(token);
        ApiKey apiKey = apiKeyService.create(user);
        user.setKey(apiKey);
        user.setEnabled(true);
        userService.save(user);
        return "Verified";
    }

    public ResendTokenResponse resendToken(String email) {
        User user = userService.getByEmail(email);
        if (user.isEnabled()) {
            throw new UserAlreadyVerifiedException(user.getName() + " is Already verified");
        }

        sendEmail(user.getEmail(), user.getName(), tokenService.regenerateToken(user));
        return new ResendTokenResponse(HttpStatus.OK.value(), "Verification Email Sent", new UserDTO(user));
    }

    private void sendEmail(String to, String name, String token) {
        final String LINK = "http://localhost:8080/api/auth/verify?token=" + token;
        emailService.sendEmail(to, " Verify Your Email Address", EmailTemplate.tokenBody(name, LINK));
    }

    public LoginResponse authenticate(LoginRequest req) {
        Authentication auth = userService.getAuthentication(req.getEmail(), req.getPassword());
        if (!auth.isAuthenticated()) {
            return new LoginResponse(HttpStatus.UNAUTHORIZED.value(), null);
        }

        return new LoginResponse(HttpStatus.ACCEPTED.value(), jwtService.generateToken(userService.getByEmail(req.getEmail())));
    }
}
