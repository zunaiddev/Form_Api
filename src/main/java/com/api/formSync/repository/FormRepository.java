package com.api.formSync.repository;

import com.api.formSync.model.Form;
import com.api.formSync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findAllByUser(User user);

    void deleteAllByIdInAndUser(List<Long> ids, User user);
}
