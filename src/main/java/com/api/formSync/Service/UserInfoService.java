package com.api.formSync.Service;

import com.api.formSync.exception.DuplicateEntrypointEmailException;
import com.api.formSync.exception.UserNotFoundException;
import com.api.formSync.model.User;
import com.api.formSync.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserInfoService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;

    public List<User> load() {
        return repo.findAll();
    }

    public User load(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }

    public User load(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }

    public User save(User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateEntrypointEmailException(String.format("User with %s email Already Exists", user.getEmail()));
        }

        return repo.save(user);
    }

    public User create(String name, String email, String password) {
        User user = repo.findByEmail(email).orElse(null);

        if (user != null) {
            if (user.isEnabled()) {
                throw new DuplicateEntrypointEmailException("User With email " + email + " already exists.");
            }

            user.setName(name);
            user.setEmail(email);
            user.setPassword(encoder.encode(password));
            user.setCreatedAt(LocalDateTime.now());
            return repo.save(user);
        }

        user = new User(name, email, encoder.encode(password));

        return repo.save(user);
    }

    public User update(User user) {
        if (user.getId() != null) {
            throw new RuntimeException("User id is provided in update function");
        }

        return repo.save(user);
    }

    public Authentication getAuthentication(String email, String password) {
        return authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public User loadWithKey(Long id) {
        return repo.findWithKeyById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not found"));
    }

    public boolean isExists(String newEmail) {
        User user = repo.findByEmail(newEmail).orElse(null);
        return user != null && user.isEnabled();
    }

    public User loadWithForms(Long id) {
        return repo.findWithFormsById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found Exception"));
    }
}
