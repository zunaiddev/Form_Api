package com.api.formSync.service;

import com.api.formSync.dto.SignupRequest;
import com.api.formSync.exception.DuplicateEntrypointEmailException;
import com.api.formSync.model.User;
import com.api.formSync.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;

    public User create(SignupRequest req) {

        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new DuplicateEntrypointEmailException(String.format("User with %s email Already Exists", req.getEmail()));
        }

        User user = new User(req.getName(), req.getEmail(), req.getPassword());

        return repo.save(user);
    }

    public User getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Invalid user id"));
    }

    public User getByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Invalid Email Address"));
    }

    public void enable(Long id) {
        User user = getById(id);
        user.setEnabled(true);
        repo.save(user);
    }
}
