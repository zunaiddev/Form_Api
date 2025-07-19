package com.api.formSync.repository;

import com.api.formSync.model.Domain;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {

    @Modifying
    @Query(value = "DELETE FROM domains d WHERE d.domain_id = :userId", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") Long userId);
}