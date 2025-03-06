package com.api.formSync.service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Email.EmailTemplate;
import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.dto.LoginRequest;
import com.api.formSync.dto.LoginResponse;
import com.api.formSync.dto.SignupRequest;
import com.api.formSync.dto.SignupResponse;
import com.api.formSync.exception.DuplicateEntrypointEmailException;
import com.api.formSync.exception.UserAlreadyVerifiedException;
import com.api.formSync.model.TempUser;
import com.api.formSync.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final ApiKeyService apiKeyService;
    private final JwtService jwtService;
    private final TempUserService tempUserService;
    @Value("${BASE_URL}")
    private String BASE_URL;

    public SignupResponse register(SignupRequest req) {

        if (userService.isExists(req.getEmail())) {
            throw new DuplicateEntrypointEmailException("user with email already exist.");
        }

        TempUser user = tempUserService.create(req);
        String token = tokenService.generateToken(user);

        sendEmail(user.getEmail(), user.getName(), token);

        return new SignupResponse("User Created Successfully. Please Verify Your Email", req);
    }

    public String isAvailable(String email) {
        if (!userService.isAvailable(email)) {
            throw new UserAlreadyVerifiedException("Unavailable");
        }

        return "Available";
    }

    public String verify(String token) {
        TempUser tempUser = tokenService.verify(token);
        User user = userService.create(tempUser);

        user.setKey(apiKeyService.create(user));
        userService.save(user);

        tempUserService.delete(tempUser);
        return "Verified";
    }

    private void sendEmail(String to, String name, String token) {
        final String LINK = "http://" + BASE_URL + "/api/auth/verify?token=" + token;
        emailService.sendEmail(to, "Verify Your Email Address", EmailTemplate.tokenBody(name, LINK));
        System.out.println("\uD83D\uDCE7 Verification Email Sent to: " + to);
    }

    public LoginResponse authenticate(LoginRequest req, HttpServletResponse response) {
        Authentication auth = userService.getAuthentication(req.getEmail(), req.getPassword());
        if (!auth.isAuthenticated()) {
            return new LoginResponse(HttpStatus.UNAUTHORIZED.value(), null);
        }

        User user = userService.getByEmail(req.getEmail());

        String accessToken = jwtService.generateToken(user, 900);
        String refreshToken = jwtService.generateToken(user, 2_592_000);

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

        User user = userService.getByEmail(email);

        if (!jwtService.validateToken(refreshToken, new UserPrincipal(user))) {
            return null;
        }

        String accessToken = jwtService.generateToken(user, 900);

        return new LoginResponse(HttpStatus.OK.value(), accessToken);
    }
}
