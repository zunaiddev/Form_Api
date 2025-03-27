package com.api.formSync.Service;

import com.api.formSync.exception.DuplicateEntrypointEmailException;
import com.api.formSync.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    public String verifyUser(String email, String token) {
        User user = userService.get(email);
        user.setEnabled(true);
        userService.update(user);
        tokenService.saveUsedToken(token);
        return "User Verified Successfully.";
    }

    public String updateEmail(String email, String newEmail, String token) {
        User user = userService.get(email);

        if (userService.isExists(newEmail)) {
            throw new DuplicateEntrypointEmailException("user already have been verified by this email.");
        }

        user.setEmail(newEmail);
        userService.update(user);

        tokenService.saveUsedToken(token);
        return "Email Updated Successfully";
    }

    public String resetPassword(String email, String password, String token) {
        User user = userService.get(email);
        user.setPassword(encoder.encode(password));
        userService.update(user);

        tokenService.saveUsedToken(token);
        return "Password Reset Successfully";
    }
}