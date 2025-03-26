package com.api.formSync.Service;

import com.api.formSync.dto.FormResponse;
import com.api.formSync.dto.UserInfo;
import com.api.formSync.exception.DuplicateEntrypointEmailException;
import com.api.formSync.model.User;
import com.api.formSync.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final AuthenticationManager authManager;
    private final FormService formService;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    public List<User> get() {
        return repo.findAll();
    }

    public User get(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Invalid Email Address"));
    }

    public User get(Long id) {
        System.out.println(repo.findById(id));
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("User not Found With id: " + id));
    }

    public User save(User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateEntrypointEmailException(String.format("User with %s email Already Exists", user.getEmail()));
        }

        return repo.save(user);
    }

    public User update(User user) {
        return repo.save(user);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public UserInfo getUser(Authentication auth) {
        if (!auth.isAuthenticated()) {
            throw new UsernameNotFoundException("Unauthorized");
        }

        User user = get(auth.getName());

        return new UserInfo(user);
    }

    public List<FormResponse> getForms(Authentication auth) {
        if (!auth.isAuthenticated()) {
            throw new UsernameNotFoundException("Unauthorized");
        }

        User user = get(auth.getName());

        return formService.get(user);
    }

    public void deleteForm(Long id, Authentication auth) {
        if (!auth.isAuthenticated()) {
            throw new UsernameNotFoundException("Unauthorised");
        }

        User user = get(auth.getName());
        formService.delete(id, user);
    }

    public Authentication getAuthentication(String email, String password) {
        return authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    public boolean isExists(String email) {
        return repo.findByEmail(email).isPresent();
    }

    public boolean isAvailable(String email) {
        User user = repo.findByEmail(email).orElse(null);
        return user == null;
    }

    public void changePassword(Long id, String newPassword) {
        User user = get(id);
        user.setPassword(encoder.encode(newPassword));
        repo.save(user);
    }
}
