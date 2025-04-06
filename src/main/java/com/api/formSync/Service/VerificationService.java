package com.api.formSync.Service;

import com.api.formSync.dto.LoginResponse;
import com.api.formSync.exception.DuplicateEntrypointEmailException;
import com.api.formSync.model.User;
import com.api.formSync.util.Purpose;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final UserInfoService userService;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;
    private final JwtService jwtService;

    public LoginResponse verifyUser(String email, String token, HttpServletResponse response) {
        User user = userService.load(email);
        user.setEnabled(true);
        userService.update(user);
        tokenService.saveUsedToken(token);

        String accessToken = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole(), "purpose", Purpose.auth), 900);
        String refreshToken = jwtService.generateToken(user.getEmail(), Map.of("purpose", Purpose.refresh_token), 2_592_000);

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(2_592_000);

        response.addCookie(cookie);

        return new LoginResponse(accessToken);
    }

    public String updateEmail(String email, String newEmail, String token) {
        User user = userService.load(email);

        if (userService.isExists(newEmail)) {
            throw new DuplicateEntrypointEmailException("user already have been verified by this email.");
        }

        user.setEmail(newEmail);
        userService.update(user);

        tokenService.saveUsedToken(token);
        return "Email Updated Successfully";
    }

    public String resetPassword(String email, String password, String token) {
        User user = userService.load(email);
        user.setPassword(encoder.encode(password));
        userService.update(user);

        tokenService.saveUsedToken(token);
        return "Password Reset Successfully";
    }
}