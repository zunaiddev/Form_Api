package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Email.EmailTemplate;
import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.dto.LoginRequest;
import com.api.formSync.dto.LoginResponse;
import com.api.formSync.dto.SignupRequest;
import com.api.formSync.dto.SignupResponse;
import com.api.formSync.exception.CouldNotFoundCookie;
import com.api.formSync.exception.InvalidTokenException;
import com.api.formSync.exception.UnauthorisedException;
import com.api.formSync.exception.UnverifiedEmailException;
import com.api.formSync.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserInfoService userInfo;
    private final EmailService emailService;
    private final JwtService jwtService;
    @Value("${BASE_URL}")
    private String BASE_URL;

    public SignupResponse register(SignupRequest req) {
        User user = userInfo.create(req.getName(), req.getEmail(), req.getPassword());
        String token = jwtService.generateToken(user.getEmail(), Map.of("purpose", "verify_user", "id", user.getId()), 900);

        emailService.sendEmail(user.getEmail(), "Please Verify Your Email.", EmailTemplate.tokenBody(user.getName(), "http://localhost:5173/verify?token=" + token));
        System.out.println("Email sent to: " + user.getEmail());
        return new SignupResponse(user.getName(), user.getEmail());
    }

    public LoginResponse authenticate(LoginRequest req, HttpServletResponse response) {
        Authentication auth = userInfo.getAuthentication(req.getEmail(), req.getPassword());
        if (!auth.isAuthenticated()) {
            throw new UnauthorisedException("Authentication Failed in Controller.");
        }

        User user = userInfo.load(req.getEmail());

        if (user.isDeleted()) {
            user.setDeleted(false);
            user.setDeleteAt(null);
            userInfo.update(user);
        }

        String accessToken = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole()), 900);
        String refreshToken = jwtService.generateToken(user.getEmail(), Map.of("role", user.getEmail()), 2_592_000);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(2_592_000);

        response.addCookie(cookie);
        return new LoginResponse(accessToken);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new CouldNotFoundCookie("refresh token Cookie is null.");
        }

        String email = jwtService.extractEmail(refreshToken);

        User user = userInfo.load(email);

        if (!jwtService.validateToken(refreshToken, new UserPrincipal(user))) {
            throw new InvalidTokenException("Invalid Token.");
        }

        String accessToken = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole()), 900);

        return new LoginResponse(accessToken);
    }

    public String resetPassword(String email) {
        User user = userInfo.load(email);
        if (!user.isEnabled()) {
            throw new UnverifiedEmailException("Please Verify Your Email: " + email);
        }

        String token = jwtService.generateToken(email, Map.of("purpose", "reset_password"), 900);

        emailService.sendEmail(email, "Reset Your Password", EmailTemplate.resetPassword(user.getName(), "http://" + BASE_URL + "/api/auth/verify/reset-password?token=" + token));
        return "Reset Password Email Sent";
    }
}