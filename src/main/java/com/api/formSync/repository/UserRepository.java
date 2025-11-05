package com.api.formSync.repository;

import com.api.formSync.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "key")
    Optional<User> findWithKeyById(Long id);

    @EntityGraph(attributePaths = "forms")
    Optional<User> findWithFormsById(Long id);

    @EntityGraph(attributePaths = {"key", "key.domains"})
    Optional<User> findWithKeyAndDomainsById(Long id);

    @EntityGraph(attributePaths = {"forms", "key"})
    Optional<User> findWithFormsAndKeyById(Long id);
}