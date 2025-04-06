package com.api.formSync.repository;

import com.api.formSync.model.ApiKey;
import com.api.formSync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByApiKey(String key);

    Optional<ApiKey> findByUser(User user);
}
