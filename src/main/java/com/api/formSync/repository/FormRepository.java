package com.api.formSync.repository;

import com.api.formSync.model.Form;
import com.api.formSync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findAllByUser(User user);

    @Modifying
    @Query("DELETE FROM Form f WHERE f.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
