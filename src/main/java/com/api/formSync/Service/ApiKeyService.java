package com.api.formSync.Service;

import com.api.formSync.Principal.ApiKeyPrincipal;
import com.api.formSync.exception.ForbiddenException;
import com.api.formSync.exception.InvalidApiKeyException;
import com.api.formSync.exception.TodayLimitReachedException;
import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Domain;
import com.api.formSync.model.User;
import com.api.formSync.repository.ApiKeyRepository;
import com.api.formSync.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
    private final ApiKeyRepository repo;

    public ApiKey create(User user, Domain domain) {
        ApiKey apiKey = new ApiKey(user, domain);
        return repo.save(apiKey);
    }

    public Authentication getAuthentication(String key, String domain) {
        ApiKey matchedKey = repo.findByApiKey(key)
                .orElseThrow(() -> new InvalidApiKeyException("Invalid API Key"));

        if (matchedKey.isLocked()) {
            throw new TodayLimitReachedException("Today Limit Reached. Api key is locked");
        }

        if (domain != null && !matchedKey.getDomains().stream().map(Domain::getDomain).toList().contains(domain)) {
            throw new ForbiddenException("You are not allowed to access this resources");
        }

        User user = matchedKey.getUser();

        if (matchedKey.getLastReset().isBefore(LocalDate.now())) {
            matchedKey.setRequestCount(0);
            matchedKey.setLastReset(LocalDate.now());
        }

        if (!user.getRole().equals(Role.ADMIN) && !user.getRole().equals(Role.ULTIMATE)) {
            final int DAILY_LIMIT = 10;

            if (matchedKey.getRequestCount() == DAILY_LIMIT) {
                matchedKey.setLocked(true);
            }

            matchedKey.setRequestCount(matchedKey.getRequestCount() + 1);
        }

        repo.save(matchedKey);
        return new UsernamePasswordAuthenticationToken(
                new ApiKeyPrincipal(matchedKey),
                null,
                AuthorityUtils.createAuthorityList(user.getRole().name())
        );
    }

    public User getUser(String key) {
        ApiKey apiKey = repo.findByApiKey(key).orElseThrow(() -> new InvalidApiKeyException("Invalid Api key"));
        System.out.println(apiKey.getId());
        return apiKey.getUser();
    }

    public ApiKey update(ApiKey apiKey) {
        return repo.save(apiKey);
    }

    public List<Domain> getDomains(Long id) {
        ApiKey key = repo.findById(id)
                .orElseThrow(() -> new InvalidApiKeyException("Could Not Found Api key With id" + id));
        return key.getDomains();
    }
}