package com.api.formSync.Service;

import com.api.formSync.model.Domain;
import com.api.formSync.repository.DomainRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DomainService {
    private final DomainRepository repo;

    public Domain create(String domain) {
        return repo.save(new Domain(domain));
    }

    public void deleteAll(List<Domain> domains) {
        repo.deleteAll(domains);
    }

    public void delete(Domain domain) {
        repo.delete(domain);
    }
}