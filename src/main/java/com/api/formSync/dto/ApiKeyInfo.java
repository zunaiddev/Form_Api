package com.api.formSync.dto;

import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Domain;
import com.api.formSync.util.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class ApiKeyInfo {
    private String apiKey;
    private List<DomainInfo> domains;
    private int requests;
    private Role role;
    private boolean active;

    public ApiKeyInfo(ApiKey apiKey) {
        List<Domain> domains = apiKey.getDomains();

        this.apiKey = apiKey.getApiKey();
        this.domains = Objects.isNull(domains) ? List.of() : domains.stream().map(DomainInfo::new).toList();
        this.requests = apiKey.getRequestCount();
        this.active = apiKey.isEnabled();
        this.role = apiKey.getRole();
    }
}