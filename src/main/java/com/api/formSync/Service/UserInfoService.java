package com.api.formSync.Service;

import com.api.formSync.Exception.ConflictException;
import com.api.formSync.Exception.UserNotFoundException;
import com.api.formSync.model.User;
import com.api.formSync.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    public User load(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    public User save(User user) {
        return repo.save(user);
    }

    public User create(String name, String email, String password) {
        User user = repo.findByEmail(email).orElse(null);

        if (user != null) {
            if (user.isEnabled()) {
                throw new ConflictException("Email already in use");
            }

            user.setName(name);
            user.setEmail(email);
            user.setPassword(encoder.encode(password));
            user.setCreatedAt(Instant.now());
            return repo.save(user);
        }

        user = new User(name, email, encoder.encode(password));

        return repo.save(user);
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new RuntimeException("User id is not provided");
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
                .orElseThrow(UserNotFoundException::new);
    }

    public User loadWithKeyAndDomains(Long id) {
        return repo.findWithKeyAndDomainsById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public boolean isExists(String newEmail) {
        User user = repo.findByEmail(newEmail).orElse(null);
        return user != null && user.isEnabled();
    }

    public User loadWithForms(Long id) {
        return repo.findWithFormsById(id)
                .orElseThrow(UserNotFoundException::new);
    }
}
