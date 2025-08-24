package com.api.formSync.dto;

import com.api.formSync.model.Domain;
import lombok.Data;

@Data
public class DomainInfo {
    private Long id;
    private String domain;

    public DomainInfo(Domain domain) {
        this.id = domain.getId();
        this.domain = domain.getDomain();
    }
}