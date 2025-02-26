package com.api.formSync.service;

import com.api.formSync.dto.FormResponse;
import com.api.formSync.dto.SignupRequest;
import com.api.formSync.dto.UserResponse;
import com.api.formSync.exception.DuplicateEntrypointEmailException;
import com.api.formSync.model.User;
import com.api.formSync.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;
    private final FormService formService;
    private final JwtService jwtService;

    public User create(SignupRequest req) {

        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new DuplicateEntrypointEmailException(String.format("User with %s email Already Exists", req.getEmail()));
        }
        String encodedPassword = encoder.encode(req.getPassword());
        User user = new User(req.getName(), req.getEmail(), encodedPassword);

        return repo.save(user);
    }

    public User getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Invalid user id"));
    }

    public User getByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Invalid Email Address"));
    }

    public void save(User user) {
        repo.save(user);
    }


    public UserResponse getUser(Long id, HttpServletRequest http) {
        User user = getAuthenticatedUser(id, http);
        return new UserResponse(user);
    }

    public List<FormResponse> getForms(Long id, HttpServletRequest http) {
        User user = getAuthenticatedUser(id, http);
        return formService.get(user);
    }

    public FormResponse deleteForm(Long userId, Long formId, HttpServletRequest http) {
        getAuthenticatedUser(userId, http);
        return formService.delete(formId);
    }

    public Authentication getAuthentication(String email, String password) {
        return authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    private User getAuthenticatedUser(Long id, HttpServletRequest http) {
        User user = getById(id);
        String email = getEmail(http);

        if (!user.getEmail().equals(email)) {
            throw new BadCredentialsException("Bad Request");
        }

        return user;
    }

    private String getEmail(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtService.extractUsername(token);
    }
}
