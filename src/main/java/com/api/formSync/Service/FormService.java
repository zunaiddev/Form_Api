package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Principal.ApiKeyPrincipal;
import com.api.formSync.dto.FormRequest;
import com.api.formSync.dto.FormResponse;
import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Form;
import com.api.formSync.model.User;
import com.api.formSync.repository.FormRepository;
import com.api.formSync.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FormService {
    private final FormRepository repo;
    private final EmailService emailService;
    private final ApiKeyService keyService;

    @Value("${TEST_API_KEY}")
    private String testKey;

    public FormResponse submit(FormRequest req, ApiKeyPrincipal details) {
        if (details.getUsername().equals(testKey)) {
            return new FormResponse(new Random().nextLong(1000), req.getName(), req.getEmail(), req.getSubject(), req.getMessage(), LocalDateTime.now());
        }

        User user = details.getUser();
        Form form = new Form(req.getName(), req.getSubject(), req.getEmail(), req.getMessage(), user);

        Form submittedForm = repo.save(form);

        Role role = user.getRole();


        if (!role.equals(Role.ADMIN) && !role.equals(Role.ULTIMATE)) {
            ApiKey apiKey = keyService.findByUser(user);

            apiKey.setRequestCount(apiKey.getRequestCount() + 1);

            if (apiKey.getRequestCount() == 10) {
                apiKey.setLocked(true);
            }

            keyService.update(apiKey);
        }

        return new FormResponse(submittedForm);
    }

    public List<FormResponse> get(User user) {
        return repo.findAllByUser(user).stream()
                .map(FormResponse::new)
                .toList();
    }

    public void delete(User user, List<Long> ids) {
        repo.deleteAllByIdInAndUser(ids, user);
    }
}