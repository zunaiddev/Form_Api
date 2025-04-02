package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.dto.PasswordRequest;
import com.api.formSync.dto.UserInfo;
import com.api.formSync.dto.UserUpdateRequest;
import com.api.formSync.exception.UnauthorisedException;
import com.api.formSync.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserInfoService userInfoService;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    public UserInfo update(UserPrincipal details, UserUpdateRequest req) {
        if (req.isInvalid()) {
            throw new ValidationException("The request must contain at least one field to update. Updating the name does not require a password, but updating the email or password requires providing the current password.");
        }

        User user = details.getUser();

        if (req.getName() != null) {
            user.setName(req.getName());
        }

        if (req.getCurrentPassword() != null && !encoder.matches(req.getCurrentPassword(), user.getPassword())) {
            System.out.println(req.getCurrentPassword());
            System.out.println(encoder.matches(req.getPassword(), details.getPassword()));
            throw new UnauthorisedException("Invalid Password");
        }

        if (req.getEmail() != null) {
            req.setEmail(req.getEmail());
        }

        if (req.getPassword() != null) {
            user.setPassword(encoder.encode(req.getPassword()));
        }

        return new UserInfo(userInfoService.update(user));
    }

    public String markAsDeleted(UserPrincipal details, PasswordRequest req, HttpServletResponse res) {
        User user = details.getUser();

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new UnauthorisedException("Invalid password");
        }

        user.setDeleted(true);
        user.setDeleteAt(LocalDateTime.now().plusDays(15));

        userInfoService.update(user);
        logout(res);
        return "Mark as deleted Delete in 15 Working days";
    }

    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return "Logged out";
    }

    public UserInfo getInfo(UserPrincipal details) {
        return new UserInfo(details.getUser());
    }
}
