package com.api.formSync.service;

import com.api.formSync.dto.FormResponse;
import com.api.formSync.dto.UserInfo;
import com.api.formSync.exception.DuplicateEntrypointEmailException;
import com.api.formSync.model.TempUser;
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

    public User create(TempUser tempUser) {

        if (repo.findByEmail(tempUser.getEmail()).isPresent()) {
            throw new DuplicateEntrypointEmailException(String.format("User with %s email Already Exists", tempUser.getEmail()));
        }

        User user = new User(tempUser.getName(), tempUser.getEmail(), tempUser.getPassword());

        return repo.save(user);
    }

    public User getByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Invalid Email Address"));
    }

    public User getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("User not Found"));
    }

    public void save(User user) {
        repo.save(user);
    }

    public void delete(Authentication auth) {
        User user = getByEmail(auth.getName());
    }

    public UserInfo getUser(Authentication auth) {
        if (!auth.isAuthenticated()) {
            throw new UsernameNotFoundException("Unauthorized");
        }

        User user = getByEmail(auth.getName());

        return new UserInfo(user);
    }

    public List<FormResponse> getForms(Authentication auth) {
        if (!auth.isAuthenticated()) {
            throw new UsernameNotFoundException("Unauthorized");
        }

        User user = getByEmail(auth.getName());

        return formService.get(user);
    }

    public void deleteForm(Long id, Authentication auth) {
        if (!auth.isAuthenticated()) {
            throw new UsernameNotFoundException("Unauthorised");
        }

        User user = getByEmail(auth.getName());
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
}
