package com.api.formSync.Service;

import com.api.formSync.model.Domain;
import com.api.formSync.repository.DomainRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DomainService {
    private final DomainRepository repo;

    public Domain create(String name) {
        return repo.findByName(name)
                .orElseGet(() -> repo.save(new Domain(name)));
    }
}