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
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserInfoService userInfoService;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final ApiKeyService apiKeyService;
    private final DomainService domainService;
    private final FormService formService;

    public UserInfo update(User user, UserUpdateRequest req) {
        if (req.isInvalid()) {
            throw new ValidationException("The request must contain at least one field to update. Updating the name does not require a password, but updating the email or password requires providing the current password.");
        }

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

            String token = jwtService.generateToken(user.getEmail(), Map.of("purpose", Purpose.UPDATE_EMAIL, "newEmail", req.getEmail()), 900);
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

    @Transactional
    public String deleteUser(UserPrincipal details, PasswordRequest req, HttpServletResponse res) {
        return "User Deleted";
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

    public ApiKeyInfo generateKey(User user, String domain) {
        if (user.getKey() != null) {
            throw new KeyCreatedException("Key is Already created. Please regenerate the key.");
        }

        Domain savedDomain = domainService.create(domain);
        ApiKey apiKey = apiKeyService.create(user, savedDomain);
        user.setKey(apiKey);
        userInfoService.update(user);
        return new ApiKeyInfo(apiKey);
    }

    public ApiKeyInfo regenerateKey(User user) {
        ApiKey apiKey = user.getKey();

        if (apiKey == null) {
            throw new InvalidApiKeyException("Could Not Found Api Key.");
        }

        apiKey.reGenerate();

        return new ApiKeyInfo(apiKeyService.update(apiKey));
    }

    public ApiKeyInfo addDomain(User user, String domain) {
        User savedUser = userInfoService.load(user.getId());
        ApiKey apiKey = savedUser.getKey();
        List<Domain> domains = apiKey.getDomains();

        if (!domains.stream().filter(d -> Objects.equals(d.getName(), domain)).toList().isEmpty()) {
            throw new DomainAlreadyExistsException("this domain is already exists");
        }

        domains.add(domainService.create(domain));
        apiKey.setDomains(domains);

        return new ApiKeyInfo(apiKeyService.update(apiKey));
    }

    public void deleteApiKey(User user) {
//        user.setKey(null);
//        userInfoService.update(user);
    }

    public void deleteDomain(User user, Long id) {
        User savedUser = userInfoService.load(user.getId());
        ApiKey apiKey = savedUser.getKey();
        List<Domain> domains = apiKey.getDomains();

        //++++++++++++++++++++++++ Needs To fix it ++++++++++++++++++++++++++++++
        Domain domain = domains.stream().filter(d -> Objects.equals(d.getId(), id))
                .findFirst().orElseThrow(() -> new DomainNotFoundException("Cound Not Found Domain With id " + id));

        apiKey.setDomains(domains.stream().filter(d -> !d.getId()
                .equals(id)).collect(Collectors.toList()));

        apiKeyService.update(apiKey);
    }

    public ApiKeyInfo getKeyInfo(User user) {
        User savedUser = userInfoService.load(user.getId());
        ApiKey apiKey = savedUser.getKey();

        if (apiKey == null) return null;

        apiKey.setRole(user.getRole());

        if (apiKey.getLastReset().isBefore(LocalDate.now())) {
            apiKey = apiKeyService.resetRequestCount(apiKey);
        }

        return new ApiKeyInfo(apiKey);
    }

    public List<FormResponse> getForms(User user) {
        User savedUser = userInfoService.load(user.getId());

        return savedUser.getForms().stream().map(FormResponse::new).toList();
    }

    public void deleteForms(User user, List<Long> ids) {
        formService.delete(user, ids);
    }
}