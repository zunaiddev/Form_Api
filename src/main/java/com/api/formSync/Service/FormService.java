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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {
    private final FormRepository repo;
    private final EmailService emailService;
    private final ApiKeyService keyService;
    private final UserInfoService userInfoService;

    @Transactional
    public FormResponse submit(FormRequest req, ApiKeyPrincipal details) {
        User user = userInfoService.loadWithForms(details.getUser().getId());
        Form form = new Form(req.getName(), req.getSubject(), req.getEmail(), req.getMessage(), user);

        Form submittedForm = repo.save(form);

        List<Form> forms = user.getForms();
        forms.add(submittedForm);
        user.setForms(forms);

        Role role = user.getRole();

        if (role.equals(Role.USER)) {
            ApiKey apiKey = keyService.findByUser(user);

            apiKey.setRequestCount(apiKey.getRequestCount() + 1);

            if (apiKey.getRequestCount() == 10) {
                apiKey.setLocked(true);
            }

            keyService.update(apiKey);
        }

        emailService.sendFormEmail(user.getEmail(), new FormResponse(submittedForm));
        return new FormResponse(submittedForm);
    }

    public List<FormResponse> get(User user) {
        return repo.findAllByUser(user).stream()
                .map(FormResponse::new)
                .toList();
    }

    public void delete(List<Form> forms) {
        repo.deleteAll(forms);
    }
}