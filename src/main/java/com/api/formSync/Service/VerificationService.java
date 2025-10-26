package com.api.formSync.Service;

import com.api.formSync.dto.ResetPasswordRequest;
import com.api.formSync.dto.SignInResponse;
import com.api.formSync.exception.DuplicateEntrypointEmailException;
import com.api.formSync.model.User;
import com.api.formSync.util.Common;
import com.api.formSync.util.UserStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final UserInfoService userService;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;
    private final GenerateTokenService generateTokenService;
    private final JwtService jwtService;

    public Object verify(String newEmail, ResetPasswordRequest req, HttpServletResponse res) {
        return new Object();
    }

    public SignInResponse verifyUser(User user, HttpServletResponse response) {
        user.setEnabled(true);
        userService.update(user);

        String accessToken = generateTokenService.accessToken(user);
        String refreshToken = generateTokenService.refreshToken(user);

        Common.setCookie(response, refreshToken);

        return new SignInResponse(accessToken, UserStatus.ACTIVE);
    }

    public String updateEmail(User user, String newEmail) {
        if (userService.isExists(newEmail)) {
            throw new DuplicateEntrypointEmailException("user already have been verified by this email.");
        }

        user.setEmail(newEmail);
        userService.update(user);

        return "Email Updated Successfully";
    }

    public String resetPassword(User user, String password) {
        user.setPassword(encoder.encode(password));
        userService.update(user);

        return "Password Reset Successfully";
    }
}