package com.api.formSync.repository;

import com.api.formSync.model.ApiKey;
import com.api.formSync.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    @EntityGraph(attributePaths = {"user", "domains", "user.forms"})
    Optional<ApiKey> findByApiKey(String key);

    Optional<ApiKey> findByUser(User user);


    Optional<ApiKey> findByUserId(Long userId);

    @EntityGraph(attributePaths = "domains")
    Optional<ApiKey> findWithDomainsByUserId(Long userId);
}