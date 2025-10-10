package com.api.formSync.Service;

import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Domain;
import com.api.formSync.repository.DomainRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DomainService {
    private final DomainRepository repo;

    public Domain create(String name) {
        return repo.findByName(name)
                .orElseGet(() -> repo.save(new Domain(name)));
    }

    public void delete(Domain domain, ApiKey key) {
        domain.setApiKeys(domain.getApiKeys()
                .stream().filter(apiKey -> !apiKey.getId().equals(key.getId()))
                .collect(Collectors.toList()));

        repo.delete(domain);
    }
}