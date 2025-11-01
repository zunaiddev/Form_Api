package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.dto.*;
import com.api.formSync.exception.ConflictException;
import com.api.formSync.exception.DomainNotFoundException;
import com.api.formSync.exception.InvalidApiKeyException;
import com.api.formSync.exception.UnauthorisedException;
import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Domain;
import com.api.formSync.model.User;
import com.api.formSync.util.Common;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
    private final EmailService emailService;
    private final JwtService jwtService;
    private final ApiKeyService apiKeyService;
    private final DomainService domainService;
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

        if (!domains.stream().filter(d -> Objects.equals(d.getName(), domain)).toList().isEmpty()) {
            throw new ConflictException("this domain is already exists");
        }

        domains.add(domainService.create(domain));
        apiKey.setDomains(domains);

        return new ApiKeyInfo(apiKeyService.update(apiKey));
    }

    public void deleteDomain(Long id, Long domainId) {
        User savedUser = userInfoService.load(id);
        ApiKey apiKey = savedUser.getKey();
        List<Domain> domains = apiKey.getDomains();

        //++++++++++++++++++++++++ Needs To fix it ++++++++++++++++++++++++++++++
        Domain domain = domains.stream().filter(d -> Objects.equals(d.getId(), id))
                .findFirst().orElseThrow(() -> new DomainNotFoundException("Cound Not Found Domain With id " + id));

        apiKey.setDomains(domains.stream().filter(d -> !d.getId()
                .equals(id)).collect(Collectors.toList()));

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
        User savedUser = userInfoService.loadWithForms(id);

        return savedUser.getForms().stream().map(FormResponse::new).toList();
    }

    public void deleteForms(Long id, List<Long> ids) {
//        formService.delete(user, ids);
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

    public void changeEmail(Long id, @Valid ChangeEmailRequest req) {
        User user = userInfoService.load(id);

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new UnauthorisedException("Password is incorrect");
        }

        if (userInfoService.isExists(req.getEmail())) {
            throw new ConflictException("Email is already in use");
        }
    }
}