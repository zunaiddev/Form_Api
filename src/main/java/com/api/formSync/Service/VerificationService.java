package com.api.formSync.Service;

import com.api.formSync.dto.ResetPasswordRequest;
import com.api.formSync.dto.SignInResponse;
import com.api.formSync.exception.DuplicateEntrypointEmailException;
import com.api.formSync.exception.InvalidPurposeException;
import com.api.formSync.exception.RequestBodyIsMissingException;
import com.api.formSync.model.User;
import com.api.formSync.util.Common;
import com.api.formSync.util.Purpose;
import com.api.formSync.util.UserStatus;
import com.api.formSync.util.VerificationToken;
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

    public Object verify(User user, VerificationToken claims, ResetPasswordRequest req, HttpServletResponse response) {
        Purpose purpose = claims.getPurpose();

        return switch (purpose) {
            case VERIFY_USER -> verifyUser(user, response);
            case UPDATE_EMAIL -> updateEmail(user, claims.getNewEmail());
            case RESET_PASSWORD -> resetPassword(user, req);
            case REACTIVATE_USER -> "Not Available yet";
            default -> throw new InvalidPurposeException("Invalid Token Purpose");
        };
    }

    private SignInResponse verifyUser(User user, HttpServletResponse response) {
        user.setEnabled(true);
        userService.update(user);

        String accessToken = generateTokenService.accessToken(user);
        String refreshToken = generateTokenService.refreshToken(user);

        Common.setCookie(response, refreshToken);

        return new SignInResponse(accessToken, UserStatus.ACTIVE);
    }

    private String updateEmail(User user, String newEmail) {
        if (userService.isExists(newEmail)) {
            throw new DuplicateEntrypointEmailException("user already have been verified by this email.");
        }

        user.setEmail(newEmail);
        userService.update(user);

        return "Email Updated Successfully";
    }

    private String resetPassword(User user, ResetPasswordRequest req) {
        if (req == null) {
            throw new RequestBodyIsMissingException("Missing Request body");
        }

        user.setPassword(encoder.encode(req.getPassword()));
        userService.update(user);

        return "Password Reset Successfully";
    }
}