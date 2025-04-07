package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Email.EmailTemplate;
import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.dto.*;
import com.api.formSync.exception.*;
import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Domain;
import com.api.formSync.model.User;
import com.api.formSync.util.Purpose;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserInfoService userInfoService;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final ApiKeyService keyService;
    private final DomainService domainService;
    private final FormService formService;

    public UserInfo update(UserPrincipal details, UserUpdateRequest req) {
        if (req.isInvalid()) {
            throw new ValidationException("The request must contain at least one field to update. Updating the name does not require a password, but updating the email or password requires providing the current password.");
        }

        User user = details.getUser();

        if (req.getName() != null) {
            user.setName(req.getName());
        }

        if (req.getCurrentPassword() != null && !encoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new UnauthorisedException("Invalid Password");
        }

        if (req.getEmail() != null) {
            if (req.getEmail().equals(user.getEmail())) {
                throw new DuplicateEntrypointEmailException("Email is Same As previous.");
            }

            if (userInfoService.isExists(req.getEmail())) {
                throw new DuplicateEntrypointEmailException("A User is Already Registered by this email.");
            }

            String token = jwtService.generateToken(user.getEmail(), Map.of("purpose", Purpose.update_email, "newEmail", req.getEmail()), 900);
            System.out.println(token);
            emailService.sendEmail(req.getEmail(), "Please Verify Your Email To update", EmailTemplate.updateEmail(user.getName(), "http://localhost:8080/api/verify?token=" + token));
        }

        if (req.getPassword() != null) {
            if (encoder.matches(req.getPassword(), user.getPassword())) {
                throw new DuplicatePasswordException("Password has already been used");
            }

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

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    public UserInfo getInfo(UserPrincipal details) {
        return new UserInfo(details.getUser());
    }

    public KeyInfo generateKey(User user, String domain) {
        if (user.getKey() != null) {
            throw new KeyCreatedException("Key is Already created. Please regenerate the key.");
        }

        Domain savedDomain = domainService.create(domain);
        ApiKey apiKey = keyService.create(user, savedDomain);
        user.setKey(apiKey);
        userInfoService.update(user);
        return new KeyInfo(apiKey);
    }

    public KeyInfo regenerateKey(User user) {
        if (user.getKey() == null) {
            throw new InvalidApiKeyException("Could Not Found Api Key.");
        }

        ApiKey key = user.getKey();
        key.reGenerate();

        return new KeyInfo(keyService.update(key));
    }

    public KeyInfo addDomain(User user, String domain) {
        ApiKey key = user.getKey();
        Domain savedDomain = domainService.create(domain);
        key.addDomain(savedDomain);

        return new KeyInfo(keyService.update(key));
    }

    public String deleteKey(User user) {
        user.setKey(null);
        userInfoService.update(user);
        return "Key Deleted Successfully";
    }

    public KeyInfo deleteDomain(User user, String domain) {
        ApiKey key = user.getKey();
        Domain savedDomain = new Domain(domain);

        key.removeDomain(savedDomain);
        return new KeyInfo(keyService.update(key));
    }

    public KeyInfo getKeyInfo(User user) {
        if (user.getKey() == null) {
            return null;
        }

        ApiKey key = user.getKey();

        return new KeyInfo(key);
    }

    public List<FormResponse> getForms(User user) {
        return formService.get(user);
    }
}