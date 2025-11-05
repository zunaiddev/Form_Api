package com.api.formSync.Dto;

import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Domain;
import com.api.formSync.util.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ApiKeyInfo {
    private String key;
    private Set<DomainInfo> domains;
    private int requests;
    private Role role;
    private boolean active;

    public ApiKeyInfo(ApiKey apiKey) {
        Set<Domain> domains = apiKey.getDomains();

        this.key = apiKey.getApiKey();
        this.domains = Objects.isNull(domains) ? Set.of() : domains.stream().map(DomainInfo::new)
                .collect(Collectors.toSet());
        this.requests = apiKey.getRequestCount();
        this.active = apiKey.isEnabled();
        this.role = apiKey.getRole();
    }
}