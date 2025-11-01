package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Email.EmailTemplate;
import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.dto.*;
import com.api.formSync.exception.CouldNotFoundTokenException;
import com.api.formSync.exception.UnauthorisedException;
import com.api.formSync.model.User;
import com.api.formSync.util.Common;
import com.api.formSync.util.Purpose;
import com.api.formSync.util.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public SignupResponse signup(SignupRequest req) {
        User user = userInfo.create(req.getName(), req.getEmail(), req.getPassword());
        String token = tokenService.verificationToken(user);

        emailService.sendEmail(user.getEmail(), "Please Verify Your Email.", EmailTemplate.tokenBody(user.getName(), BASE_URL + "/verify?token=" + token));
        log.info("Verification Email Sent to: {}", user.getEmail());
        return new SignupResponse(user.getName(), user.getEmail());
    }

    public SignInResponse signIn(LoginRequest req, HttpServletResponse response) {
        Authentication auth = userInfo.getAuthentication(req.getEmail(), req.getPassword());

        if (!auth.isAuthenticated()) {
            throw new UnauthorisedException("Authentication Failed.");
        }

        User user = ((UserPrincipal) auth.getPrincipal()).getUser();

        if (user.getDeleteAt() != null) {
            return new SignInResponse(tokenService.reactivateToken(user), UserStatus.PENDING_DELETION);
        }

        String accessToken = tokenService.accessToken(user);
        String refreshToken = tokenService.refreshToken(user);

        response.addCookie(Common.getCookie(refreshToken));
        return new SignInResponse(accessToken, UserStatus.ACTIVE);
    }

    //TODO: Logout user if status not active during refresh token
    public SignInResponse refreshToken(String token) {
        if (token == null) {
            throw new CouldNotFoundTokenException("Refresh Token is Missing");
        }

        Claims claims = jwtService.extractClaims(token);
        Purpose purpose = Purpose.from(claims.get("purpose"));

        if (purpose == null || !purpose.equals(Purpose.REFRESH_TOKEN)) {
            log.warn("Invalid Token Used at REFRESH_TOKEN");
            throw new JwtException("Invalid Token Type or Token type is missing");
        }

        long id = Long.parseLong(claims.getSubject());
        User user = userInfo.load(id);

        if (user.isLocked()) throw new LockedException("User is Locked");

        UserStatus status = Objects.isNull(user.getDeleteAt()) ?
                UserStatus.ACTIVE : UserStatus.PENDING_DELETION;

        String accessToken = status.equals(UserStatus.ACTIVE) ?
                tokenService.accessToken(user) : null;

        return new SignInResponse(accessToken, status);
    }

    public EmailResponse forgetPassword(String email) {
        User user = userInfo.load(email);

        if (!user.isEnabled()) {
            throw new DisabledException("User is Disabled");
        }

        String token = tokenService.resetPasswordToken(user);

        emailService.sendEmail(email, "Reset Your Password", EmailTemplate.resetPassword(user.getName(), "http://" + BASE_URL + "/api/auth/verify/reset-password?token=" + token));
        return new EmailResponse(email);
    }
}