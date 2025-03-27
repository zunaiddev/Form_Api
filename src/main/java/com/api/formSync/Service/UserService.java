package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Email.EmailTemplate;
import com.api.formSync.dto.FormResponse;
import com.api.formSync.dto.UserInfo;
import com.api.formSync.exception.DuplicateEntrypointEmailException;
import com.api.formSync.exception.ForbiddenException;
import com.api.formSync.exception.UnauthorisedException;
import com.api.formSync.model.User;
import com.api.formSync.repository.UserRepository;
import com.api.formSync.util.Validator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final AuthenticationManager authManager;
    private final FormService formService;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    public User create(String name, String email, String password) {
        User user = repo.findByEmail(email).orElse(null);
        String encodedPassword = encoder.encode(password);

        if (user != null) {
            if (user.isEnabled()) {
                throw new DuplicateEntrypointEmailException("user with email already exist.");
            }

            user.setName(name);
            user.setEmail(email);
            user.setPassword(encodedPassword);
            user.setCreatedAt(LocalDateTime.now());
            return repo.save(user);
        }

        user = new User(name, email, encodedPassword);

        return repo.save(user);
    }

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

    public User update(Authentication auth, Long id, Map<String, Object> updates) {
        User user = get(id);

        if (!auth.getName().equals(user.getEmail())) {
            throw new AccessDeniedException("Forbidden");
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    if (!Validator.name(value.toString())) {
                        throw new ValidationException("Invalid name Format");
                    }

                    user.setName(value.toString());
                    break;
                case "email":
                    if (!Validator.email(value.toString())) {
                        throw new ValidationException("Invalid email");
                    }

                    if (!updates.containsKey("password") || !encoder.matches(updates.get("password").toString(), user.getPassword())) {
                        throw new ValidationException("wrong or null password");
                    }

                    if (user.getEmail().equals(value)) {
                        throw new ValidationException("Email is same as previous.");
                    }

                    if (isExists(value.toString())) {
                        throw new DuplicateEntrypointEmailException("Email in use");
                    }

                    String token = jwtService.generateToken(user.getEmail(), Map.of("purpose", "update_email", "newEmail", value), 900);
                    emailService.sendEmail(value.toString(), "Update Email Address", EmailTemplate.updateEmail(user.getName(), "http://localhost:8080/api/verify?token=" + token));
                    break;
                case "newPassword":
                    if (!Validator.password(value.toString())) {
                        throw new ValidationException("Invalid new Password Format");
                    }

                    if (!updates.containsKey("password") || !encoder.matches(updates.get("password").toString(), user.getPassword())) {
                        throw new ValidationException("wrong or null password");
                    }

                    user.setPassword(encoder.encode((String) value));
                    break;
                case "regenerateKey":
                    if (!Validator.isBool(value)) {
                        throw new ValidationException("regenerateKey Should be a boolean");
                    }

                    if (user.getKey() == null) {
                        throw new ValidationException("Key is not set");
                    }

                    if ((boolean) value) {
                        user.getKey().reGenerate();
                    }
                    break;
            }
        });

        return update(user);
    }

    public void deleteImmediately(Long id) {
        User user = get(id);
        formService.delete(id, user);
        repo.deleteById(id);
    }

    public UserInfo getUser(Authentication auth) {
        if (!auth.isAuthenticated()) {
            throw new UsernameNotFoundException("Unauthorized");
        }

        User user = get(auth.getName());
        System.out.println(user);

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
        Optional<User> user = repo.findByEmail(email);
        return user.isPresent() && user.get().isEnabled();
    }

    public boolean isAvailable(String email) {
        User user = repo.findByEmail(email).orElse(null);
        return user == null;
    }

    public String markAsDeleted(Authentication auth, Long id, String password, HttpServletResponse response) {
        User user = get(id);

        if (!auth.getName().equals(user.getEmail())) {
            throw new ForbiddenException("Forbidden");
        }

        if (!encoder.matches(password, user.getPassword())) {
            throw new UnauthorisedException("Invalid password");
        }

        user.setDeleted(true);
        user.setDeleteAt(LocalDateTime.now().plusDays(15));

        update(user);
        logout(response);
        return "Mark as deleted Delete in 3 Working days";
    }

    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return "Logged out";
    }
}
