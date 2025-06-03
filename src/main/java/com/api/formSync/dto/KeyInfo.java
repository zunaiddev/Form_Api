package com.api.formSync.dto;

import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Domain;
import com.api.formSync.util.Role;
import lombok.Data;

import java.util.List;

@Data
public class KeyInfo {
    private String key;
    private List<String> domains;
    private int requests;
    private Role role;
    private boolean isActive;

    public KeyInfo(ApiKey key) {
        this.key = key.getApiKey();
        this.domains = key.getDomains().stream().map(Domain::getDomain).toList();
        this.requests = key.getRequestCount();
        this.isActive = !key.isLocked();
        this.role = key.getRole();
    }
}