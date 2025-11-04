package com.api.formSync.Service;

import com.api.formSync.Dto.ResetPasswordRequest;
import com.api.formSync.Dto.SignInResponse;
import com.api.formSync.Dto.VerificationResponse;
import com.api.formSync.Exception.ConflictException;
import com.api.formSync.Exception.RequestBodyIsMissingException;
import com.api.formSync.model.User;
import com.api.formSync.util.Common;
import com.api.formSync.util.Purpose;
import com.api.formSync.util.UserStatus;
import com.api.formSync.util.VerificationToken;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final UserInfoService userService;
    private final PasswordEncoder encoder;
    private final GenerateTokenService generateTokenService;

    public VerificationResponse<?> verify(User user, VerificationToken claims, ResetPasswordRequest req, HttpServletResponse response) {
        Purpose purpose = claims.getPurpose();

        return switch (purpose) {
            case VERIFY_USER -> verifyUser(user, response);
            case UPDATE_EMAIL -> updateEmail(user, claims.getNewEmail());
            case RESET_PASSWORD -> resetPassword(user, req);
            case REACTIVATE -> reactivate(user, response);
            default -> throw new JwtException("Invalid Token Purpose");
        };
    }

    private VerificationResponse<SignInResponse> verifyUser(User user, HttpServletResponse response) {
        user.setEnabled(true);
        userService.update(user);

        String accessToken = generateTokenService.accessToken(user);
        String refreshToken = generateTokenService.refreshToken(user);

        Common.setCookie(response, refreshToken);

        return new VerificationResponse<>("Account Verified Successfully",
                "Your email has been confirmed and your account is now active. You can continue using the service securely.",
                new SignInResponse(accessToken, UserStatus.ACTIVE, null));
    }

    private VerificationResponse<?> updateEmail(User user, String newEmail) {
        if (userService.isExists(newEmail)) {
            throw new ConflictException("Email already in use");
        }

        user.setEmail(newEmail);
        userService.update(user);

        return new VerificationResponse<>("Email Updated Successfully",
                "Your email address has been changed. For security reasons, please use this updated email when signing in.Your email address has been changed. For security reasons, please use this updated email when signing in.",
                null);
    }

    private VerificationResponse<?> resetPassword(User user, ResetPasswordRequest req) {
        if (req == null) {
            throw new RequestBodyIsMissingException("Missing Request body");
        }

        if (encoder.matches(req.getPassword(), user.getPassword())) {
            throw new ConflictException("New password cannot be the same as the old password");
        }

        user.setPassword(encoder.encode(req.getPassword()));
        userService.update(user);

        return new VerificationResponse<>("Password Changed Successfully",
                "Your password has been updated. You can now log in using your new password.", null);
    }

    private VerificationResponse<SignInResponse> reactivate(User user, HttpServletResponse response) {
        user.setDeleteAt(null);
        userService.update(user);

        String accessToken = generateTokenService.accessToken(user);
        String refreshToken = generateTokenService.refreshToken(user);

        response.addCookie(Common.getCookie(refreshToken));
        return new VerificationResponse<>("Account Reactivated",
                "Your account has been restored and is active again. If you did not request this reactivation, please contact support immediately.",
                new SignInResponse(accessToken, UserStatus.ACTIVE, null));
    }
}