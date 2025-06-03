package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Email.EmailTemplate;
import com.api.formSync.Principal.ApiKeyPrincipal;
import com.api.formSync.dto.FormRequest;
import com.api.formSync.dto.FormResponse;
import com.api.formSync.exception.ForbiddenException;
import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Form;
import com.api.formSync.model.User;
import com.api.formSync.repository.FormRepository;
import com.api.formSync.util.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FormService {
    private final FormRepository repo;
    private final EmailService emailService;
    private final ApiKeyService keyService;

    public FormResponse submit(FormRequest req, ApiKeyPrincipal details) {
        User user = details.getUser();
        Form form = new Form(req.getName(), req.getSubject(), req.getEmail(), req.getMessage(), user);
        Form submittedForm = repo.save(form);

        Role role = user.getRole();

        emailService.sendEmailAsync(user.getEmail(), "New Form Submission Received.", EmailTemplate.formResponse(user.getName(), form.getName(), form.getEmail(), form.getSubject(), form.getMessage()));

        if (role.equals(Role.ADMIN)) {
            emailService.sendEmailAsync(req.getEmail(), "Thank You for Contacting Zunaid", EmailTemplate.adminBody(req.getName()));
        }

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

    public void delete(User user, Long id) {
        Form form = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not found any form with id " + id));

        if (!Objects.equals(form.getUser().getId(), user.getId())) {
            System.out.println("Inside if");
            throw new ForbiddenException("You are not allowed to delete this form.");
        }

        repo.deleteById(form.getId());
    }
}
