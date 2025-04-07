package com.api.formSync.Service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Email.EmailTemplate;
import com.api.formSync.Principal.ApiKeyPrincipal;
import com.api.formSync.dto.FormRequest;
import com.api.formSync.dto.FormResponse;
import com.api.formSync.model.Form;
import com.api.formSync.model.User;
import com.api.formSync.repository.FormRepository;
import com.api.formSync.util.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FormService {
    private final FormRepository repo;
    private final EmailService emailService;

    public FormResponse submit(FormRequest req, ApiKeyPrincipal details) {
        User user = details.getUser();
        Form form = new Form(req.getName(), req.getSubject(), req.getEmail(), req.getMessage(), user);
        Form submittedForm = repo.save(form);

        if (user.getRole().equals(Role.ADMIN)) {
            emailService.sendEmailAsync(req.getEmail(), "Thank You for Contacting Zunaid", EmailTemplate.adminBody(req.getName()));
        }

        return new FormResponse(submittedForm);
    }

    public List<FormResponse> get(User user) {
        return repo.findAllByUser(user).stream()
                .map(FormResponse::new)
                .toList();
    }
}
