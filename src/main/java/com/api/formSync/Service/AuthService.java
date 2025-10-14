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
import com.api.formSync.util.Common;
import com.api.formSync.util.Purpose;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserInfoService userInfo;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final GenerateTokenService tokenService;
    @Value("${BASE_URL}")
    private String BASE_URL;

    public SignupResponse register(SignupRequest req) {
        User user = userInfo.create(req.getName(), req.getEmail(), req.getPassword());
        String token = tokenService.verifyUser(user);

        emailService.sendEmail(user.getEmail(), "Please Verify Your Email.", EmailTemplate.tokenBody(user.getName(), BASE_URL + "/verify?token=" + token));
        log.info("Verification Email Sent to: {}", user.getEmail());
        return new SignupResponse(user.getName(), user.getEmail());
    }

    public LoginResponse authenticate(LoginRequest req, HttpServletResponse response) {
        Authentication auth = userInfo.getAuthentication(req.getEmail(), req.getPassword());

        if (!auth.isAuthenticated()) {
            throw new UnauthorisedException("Authentication Failed.");
        }

        User user = userInfo.load(req.getEmail());

        if (user.getDeleteAt() != null) {
            user.setDeleteAt(null);
            userInfo.update(user);
        }

        String accessToken = tokenService.auth(user);
        String refreshToken = tokenService.refresh(user);

        Common.setCookie(response, refreshToken);
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

        if (jwtService.extractClaims(refreshToken).get("purpose") == null || !jwtService.extractClaims(refreshToken).get("purpose").equals(Purpose.REFRESH_TOKEN.name())) {
            log.warn("Purpose is missing or not found");
            throw new InvalidTokenException("Invalid Token Type or Token type is missing");
        }

        String accessToken = tokenService.auth(user);

        return new LoginResponse(accessToken);
    }

    public String forgetPassword(String email) {
        User user = userInfo.load(email);

        if (!user.isEnabled()) {
            throw new UnverifiedEmailException("Please Verify Your Email: " + email);
        }

        String token = tokenService.resetPassword();

        emailService.sendEmail(email, "Reset Your Password", EmailTemplate.resetPassword(user.getName(), "http://" + BASE_URL + "/api/auth/verify/reset-password?token=" + token));
        return "Reset Password Email Sent";
    }

    public void logout(HttpServletResponse response) {
        response.setHeader("Set-Cookie",
                String.format("refresh_token=%s; Max-Age=%d; Path=/; Secure; HttpOnly; SameSite=None", null, 0));
    }
}