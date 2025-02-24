package com.api.formSync.service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Email.EmailTemplate;
import com.api.formSync.dto.ResendTokenResponse;
import com.api.formSync.dto.SignupRequest;
import com.api.formSync.dto.SignupResponse;
import com.api.formSync.dto.UserDTO;
import com.api.formSync.exception.UserAlreadyVerifiedException;
import com.api.formSync.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;

    public SignupResponse register(SignupRequest req) {
        User user = userService.create(req);
        sendEmail(user.getEmail(), user.getName(), tokenService.generateToken(user), user.getId());
        return new SignupResponse("User Created Successfully. Please Verify Your Email", new UserDTO(user));
    }

    public String verify(String token, Long id) {
        tokenService.verify(token, id);
        userService.enable(id);
        return "Verified";
    }

    public ResendTokenResponse resendToken(String email) {
        User user = userService.getByEmail(email);
        if (user.isEnabled()) {
            throw new UserAlreadyVerifiedException(user.getName() + " is Already verified");
        }

        sendEmail(user.getEmail(), user.getName(), tokenService.regenerateToken(user), user.getId());
        return new ResendTokenResponse(HttpStatus.OK.value(), "Verification Email Sent", new UserDTO(user));
    }

    private void sendEmail(String to, String name, String token, Long id) {
        final String LINK = "http://localhost:8080/api/auth/verify?token=" + token + "&id=" + id;
        emailService.sendEmail(to, " Verify Your Email Address", EmailTemplate.tokenBody(name, LINK));
    }
}
