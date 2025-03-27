package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Email.EmailTemplate;
import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.dto.LoginRequest;
import com.api.formSync.dto.LoginResponse;
import com.api.formSync.dto.SignupRequest;
import com.api.formSync.dto.SignupResponse;
import com.api.formSync.exception.UserAlreadyVerifiedException;
import com.api.formSync.model.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final EmailService emailService;
    private final ApiKeyService apiKeyService;
    private final JwtService jwtService;
    @Value("${BASE_URL}")
    private String BASE_URL;

    public SignupResponse register(SignupRequest req) {
        User user = userService.create(req.getName(), req.getEmail(), req.getPassword());
        String token = jwtService.generateToken(user.getEmail(), Map.of("purpose", "verify_user"), 900);

        sendEmail(user.getEmail(), user.getName(), token);
        return new SignupResponse(user.getName(), user.getEmail(), "User Created Successfully. Please Verify Your Email");
    }

    public String isAvailable(String email) {
        if (!userService.isAvailable(email)) {
            throw new UserAlreadyVerifiedException("Unavailable");
        }

        return "Available";
    }

    public LoginResponse authenticate(LoginRequest req, HttpServletResponse response) {
        Authentication auth = userService.getAuthentication(req.getEmail(), req.getPassword());
        if (!auth.isAuthenticated()) {
            return new LoginResponse(HttpStatus.UNAUTHORIZED.value(), null);
        }

        User user = userService.get(req.getEmail());

        if (user.isDeleted()) {
            user.setDeleted(false);
            user.setDeleteAt(null);
            userService.update(user);
        }

        String accessToken = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole()), 900);
        String refreshToken = jwtService.generateToken(user.getEmail(), Map.of("role", user.getEmail()), 2_592_000);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(2_592_000);

        response.addCookie(cookie);
        return new LoginResponse(HttpStatus.OK.value(), accessToken);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (refreshToken == null) {
            return null;
        }

        String email;

        try {
            email = jwtService.extractEmail(refreshToken);
        } catch (Exception exp) {
            return null;
        }

        User user = userService.get(email);

        if (!jwtService.validateToken(refreshToken, new UserPrincipal(user))) {
            return null;
        }

        String accessToken = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole()), 900);

        return new LoginResponse(HttpStatus.OK.value(), accessToken);
    }

    public String resetPassword(String email) {
        User user = userService.get(email);
        if (!user.isEnabled()) {
            throw new EntityNotFoundException("Please Verify Your Email: " + email);
        }

        String token = jwtService.generateToken(email, Map.of("purpose", "reset_password"), 900);

        emailService.sendEmail(email, "Reset Your Password", EmailTemplate.resetPassword(user.getName(), "http://" + BASE_URL + "/api/auth/verify/reset-password?token=" + token));
        return "Reset Password Email Sent";
    }

    private void sendEmail(String to, String name, String token) {
        final String LINK = "http://" + BASE_URL + "/api/auth/verify?token=" + token;
        emailService.sendEmail(to, "Verify Your Email Address", EmailTemplate.tokenBody(name, LINK));
        System.out.println("Verification Email Sent to: " + to);
    }
}
