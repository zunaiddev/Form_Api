package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.dto.*;
import com.api.formSync.exception.ConflictException;
import com.api.formSync.exception.InvalidApiKeyException;
import com.api.formSync.exception.UnauthorisedException;
import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Domain;
import com.api.formSync.model.User;
import com.api.formSync.util.Common;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserInfoService userInfoService;
    private final PasswordEncoder encoder;
    private final ApiKeyService apiKeyService;
    private final DomainService domainService;
    private final GenerateTokenService generateTokenService;
    private final EmailService emailService;
    private final FormService formService;

    public UserInfo userInfo(Long id) {
        return new UserInfo(userInfoService.load(id));
    }

    public UserInfo updateUser(Long id, UserUpdateRequest req) {
        User user = userInfoService.load(id);
        user.setName(req.getName());

        return new UserInfo(userInfoService.update(user));
    }

    public void deleteUser(long id, PasswordRequest req, HttpServletResponse res) {
        User user = userInfoService.load(id);

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new UnauthorisedException("Password is incorrect");
        }

        user.setDeleteAt(LocalDateTime.now().plusDays(3L));
        userInfoService.update(user);

        res.addCookie(Common.getEmptyCookie());
    }

    @Transactional
    public ApiKeyInfo generateKey(Long id) {
        User user = userInfoService.loadWithKey(id);

        if (user.getKey() != null) {
            throw new ConflictException("Api Key is Already created. Please regenerate the key.");
        }

        user.setKey(new ApiKey(user));

        User saveduser = userInfoService.save(user);
        return new ApiKeyInfo(saveduser.getKey());
    }

    @Transactional
    public ApiKeyInfo regenerateKey(Long id) {
        User user = userInfoService.loadWithKey(id);
        ApiKey apiKey = user.getKey();

        if (apiKey == null) {
            throw new InvalidApiKeyException("Could Not Found Api Key.");
        }

        apiKey.reGenerate();

        return new ApiKeyInfo(apiKeyService.update(apiKey));
    }

    public ApiKeyInfo updateStatus(long id, boolean status) {
        User user = userInfoService.loadWithKey(id);
        ApiKey apiKey = user.getKey();

        if (apiKey == null) {
            throw new InvalidApiKeyException("Could Not Found Api Key.");
        }

        if (apiKey.isEnabled() == status) {
            throw new ConflictException("Api Key is already " + (status ? "enabled" : "disabled"));
        }

        apiKey.setEnabled(status);
        return new ApiKeyInfo(apiKeyService.update(apiKey));
    }

    //ToDo: Needs to check domain validity
    @Transactional
    public ApiKeyInfo addDomain(Long id, String domain) {
        User savedUser = userInfoService.loadWithKey(id);
        ApiKey apiKey = savedUser.getKey();
        List<Domain> domains = apiKey.getDomains();

        for (Domain d : domains) {
            if (d.getName().equals(domain)) throw new ConflictException("Domain Already Exists");
        }

        domains.add(domainService.create(domain));
        apiKey.setDomains(domains);

        return new ApiKeyInfo(apiKeyService.update(apiKey));
    }

    public void deleteDomain(Long id, Long domainId) {
        User savedUser = userInfoService.loadWithKey(id);
        ApiKey apiKey = savedUser.getKey();
        List<Domain> domains = apiKey.getDomains();

        domains = domains.stream().filter(domain -> !Objects.equals(domain.getId(), domainId))
                .collect(Collectors.toList());

        apiKey.setDomains(domains);

        apiKeyService.update(apiKey);
    }

    public ApiKeyInfo getKeyInfo(Long id) {
        User user = userInfoService.loadWithKey(id);
        ApiKey apiKey = user.getKey();

        if (apiKey == null) return null;

        apiKey.setRole(user.getRole());

        if (apiKey.getLastReset().isBefore(LocalDate.now())) {
            apiKey = apiKeyService.resetRequestCount(apiKey);
        }

        return new ApiKeyInfo(apiKey);
    }

    @Transactional
    public List<FormResponse> getForms(Long id) {
//        User savedUser = userInfoService.loadWithForms(id);

//        return savedUser.getForms().stream().map(FormResponse::new).toList();

        return List.of(
                new FormResponse(1L, "John Doe", "john@gmail.com", "Subject 1", "Message 1", LocalDateTime.now()),
                new FormResponse(2L, "Jane Smith", "jane@gmail.com", "Subject 2", "Message 2", LocalDateTime.now()),
                new FormResponse(3L, "Bob Johnson", "bob@gmail.com", "Subject 3", "Message 3", LocalDateTime.now())
        );
    }

    @Transactional
    public void deleteForms(Long id, List<Long> ids) {
        User user = userInfoService.load(id);
        formService.delete(user, ids);
    }

    public void changePassword(Long id, ChangePasswordRequest req) {
        if (req.getPassword().equals(req.getNewPassword())) {
            throw new ConflictException("New Password must be different from Old Password");
        }

        User user = userInfoService.load(id);

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new UnauthorisedException("Password is incorrect");
        }

        user.setPassword(encoder.encode(req.getNewPassword()));
        userInfoService.update(user);

//        emailService.sendPasswordChangeNotification(user.getEmail(), user.getName());
    }

    public EmailResponse changeEmail(Long id, ChangeEmailRequest req) {
        User user = userInfoService.load(id);

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new UnauthorisedException("Password is incorrect");
        }

        if (userInfoService.isExists(req.getEmail())) {
            throw new ConflictException("Email is already in use");
        }

        String token = generateTokenService.changeEmailToken(user, req.getEmail());

        emailService.sendEmailChangeConfirmationEmail(req.getEmail(), user.getName(), token);

        return new EmailResponse(req.getEmail());
    }
}