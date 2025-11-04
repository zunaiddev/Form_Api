package com.api.formSync.Service;

import com.api.formSync.Exception.InvalidApiKeyException;
import com.api.formSync.Principal.ApiKeyPrincipal;
import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Domain;
import com.api.formSync.model.User;
import com.api.formSync.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
    private final ApiKeyRepository repo;

    public Authentication getAuthentication(String key, String domain) {
        ApiKey matchedKey = repo.findByApiKey(key)
                .orElseThrow(() -> new InvalidApiKeyException("Invalid API Key"));

        List<String> domains = matchedKey.getDomains()
                .stream().map(Domain::getName).toList();

//        if (!domains.contains(domain)) {
//            throw new ForbiddenException("You are not allowed to access this resource");
//        }

        if (matchedKey.getLastReset().isBefore(Instant.now())) {
            matchedKey = resetRequestCount(matchedKey);
        }

        return new UsernamePasswordAuthenticationToken(
                new ApiKeyPrincipal(matchedKey),
                null,
                AuthorityUtils.createAuthorityList(matchedKey.getRole().name())
        );
    }

    public ApiKey findByUser(User user) {
        return repo.findByUser(user).orElseThrow(() -> new InvalidApiKeyException("Invalid API Key"));
    }

    public ApiKey findWithDomainsByUserId(Long userId) {
        return repo.findWithDomainsByUserId(userId)
                .orElseThrow(() -> new InvalidApiKeyException("Invalid API Key"));
    }

    public ApiKey update(ApiKey apiKey) {
        if (apiKey.getId() == null) throw new RuntimeException("Api Key id is null");

        return repo.save(apiKey);
    }

    public void delete(User user) {
        ApiKey apiKey = findByUser(user);

        apiKey.setUser(null);
        update(apiKey);

        repo.delete(apiKey);
    }

    public ApiKey resetRequestCount(ApiKey apiKey) {
        apiKey.setRequestCount(0);
        apiKey.setLastReset(Instant.now());
        apiKey.setLocked(false);
        return update(apiKey);
    }
}