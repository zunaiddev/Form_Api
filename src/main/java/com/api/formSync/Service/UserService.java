package com.api.formSync.Service;

import com.api.formSync.Dto.*;
import com.api.formSync.Email.EmailService;
import com.api.formSync.Exception.ConflictException;
import com.api.formSync.Exception.InvalidApiKeyException;
import com.api.formSync.Exception.UnauthorisedException;
import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Domain;
import com.api.formSync.model.Form;
import com.api.formSync.model.User;
import com.api.formSync.util.Common;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

        user.setDeleteAt(Instant.now().plus(3, ChronoUnit.DAYS));
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

    @Transactional
    public ApiKeyInfo addDomain(Long id, String domain) {
        User savedUser = userInfoService.loadWithKeyAndDomains(id);
        ApiKey apiKey = savedUser.getKey();
        Set<Domain> domains = apiKey.getDomains();

        for (Domain d : domains) {
            if (d.getName().equals(domain)) throw new ConflictException("Domain Already Exists");
        }

        domains.add(domainService.create(domain));
        apiKey.setDomains(domains);

        return new ApiKeyInfo(apiKeyService.update(apiKey));
    }

    public void removeDomain(Long id, Long domainId) {
        ApiKey apiKey = apiKeyService.findWithDomainsByUserId(id);
        Set<Domain> domains = apiKey.getDomains();
        System.out.println("Domains before removal: " + domains);

        domains = domains.stream().filter(domain -> !Objects.equals(domain.getId(), domainId))
                .collect(Collectors.toSet());

        apiKey.setDomains(domains);

        apiKeyService.update(apiKey);
    }

    public ApiKeyInfo getKeyInfo(Long id) {
        User user = userInfoService.loadWithKeyAndDomains(id);
        ApiKey apiKey = user.getKey();

        if (apiKey == null) return null;

        apiKey.setRole(user.getRole());

        if (apiKey.getLastReset().isBefore(Instant.now())) {
            apiKey = apiKeyService.resetRequestCount(apiKey);
        }

        return new ApiKeyInfo(apiKey);
    }

    @Transactional
    public List<FormResponse> getForms(Long id) {
        User savedUser = userInfoService.loadWithForms(id);

        return savedUser.getForms().stream().map(FormResponse::new).toList();
    }

    @Transactional
    public void deleteForms(Long userId, List<Long> ids) {
        User user = userInfoService.loadWithForms(userId);

        List<Form> formsToDel = user.getForms()
                .stream().filter(form -> ids.contains(form.getId()))
                .collect(Collectors.toList());

        List<Form> forms = user.getForms();
        forms.removeAll(formsToDel);
        user.setForms(forms);

        userInfoService.update(user);

        formService.delete(formsToDel);
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