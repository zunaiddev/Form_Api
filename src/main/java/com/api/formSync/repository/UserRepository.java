package com.api.formSync.repository;

import com.api.formSync.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.enabled = false AND u.createdAt < :threshold")
    void deleteUnverifiedUsers(LocalDateTime threshold);
}
