package com.api.formSync.service;

import com.api.formSync.Email.EmailService;
import com.api.formSync.Email.EmailTemplate;
import com.api.formSync.dto.FormRequest;
import com.api.formSync.dto.FormResponse;
import com.api.formSync.exception.ResourceNotFoundException;
import com.api.formSync.model.Form;
import com.api.formSync.model.User;
import com.api.formSync.repository.FormRepository;
import com.api.formSync.util.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FormService {
    private final FormRepository repo;
    private final ApiKeyService keyService;
    private final EmailService emailService;

    public FormResponse submit(FormRequest req, HttpServletRequest http) {
        User user = keyService.getUser(getKey(http));
        Form form = new Form(req.getName(), req.getSubject(), req.getEmail(), req.getMessage(), user);
        Form submittedForm = repo.save(form);

        if (user.getRole().equals(Role.ADMIN)) {
            emailService.sendEmailAsync(req.getEmail(), "Thank You for Contacting Me", EmailTemplate.adminBody(req.getName()));
        }

        return new FormResponse(submittedForm);
    }

    public List<FormResponse> get(User user) {
        return repo.findAllByUser(user).stream()
                .map(FormResponse::new)
                .collect(Collectors.toList());
    }

    public void delete(Long id, User user) {
        Form form = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found."));

        if (!form.getUser().equals(user)) {
            throw new AccessDeniedException("You are not authorized to delete this form");
        }

        repo.delete(form);
    }

    private String getKey(HttpServletRequest http) {
        System.out.println("APi KEY: " + http.getHeader("X-API-KEY"));
        return http.getHeader("X-API-KEY");
    }
}
