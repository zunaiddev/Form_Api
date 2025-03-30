package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.dto.UserInfo;
import com.api.formSync.dto.UserUpdateRequest;
import com.api.formSync.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    public UserInfo update(UserPrincipal details, UserUpdateRequest req) {
        System.out.println(req + " end");
        System.out.println("is Valid: " + req.isValid());
        return new UserInfo(details.getUser());
    }

//    public String markAsDeleted(Authentication auth, Long id, String password, HttpServletResponse response) {
//        User user = get(id);
//
//        if (!auth.getName().equals(user.getEmail())) {
//            throw new ForbiddenException("Forbidden");
//        }
//
//        if (!encoder.matches(password, user.getPassword())) {
//            throw new UnauthorisedException("Invalid password");
//        }
//
//        user.setDeleted(true);
//        user.setDeleteAt(LocalDateTime.now().plusDays(15));
//
//        update(user);
//        logout(response);
//        return "Mark as deleted Delete in 3 Working days";
//    }

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
