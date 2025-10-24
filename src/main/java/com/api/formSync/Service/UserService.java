package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.dto.*;
import com.api.formSync.exception.DomainAlreadyExistsException;
import com.api.formSync.exception.DomainNotFoundException;
import com.api.formSync.exception.InvalidApiKeyException;
import com.api.formSync.exception.KeyCreatedException;
import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Domain;
import com.api.formSync.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public UserInfo update(Long id, UserUpdateRequest req) {
        User user = userInfoService.load(id);
        user.setName(req.getName());

        return new UserInfo(userInfoService.save(user));
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

    public UserInfo getInfo(Long id) {
        return new UserInfo(userInfoService.load(id));
    }

    @Transactional
    public ApiKeyInfo generateKey(Long id, String domain) {
        User user = userInfoService.loadWithKey(id);

        if (user.getKey() != null) {
            throw new KeyCreatedException("Api Key is Already created. Please regenerate the key.");
        }

        Domain savedDomain = domainService.create(domain);
        ApiKey apiKey = apiKeyService.create(user, savedDomain);
        user.setKey(apiKey);
        userInfoService.update(user);
        return new ApiKeyInfo(apiKey);
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

    public ApiKeyInfo addDomain(Long id, String domain) {
        User savedUser = userInfoService.load(id);
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
}