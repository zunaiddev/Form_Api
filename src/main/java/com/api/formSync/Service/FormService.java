package com.api.formSync.Service;

import com.api.formSync.Dto.FormRequest;
import com.api.formSync.Dto.FormResponse;
import com.api.formSync.Email.EmailService;
import com.api.formSync.Principal.ApiKeyPrincipal;
import com.api.formSync.model.ApiKey;
import com.api.formSync.model.Form;
import com.api.formSync.model.User;
import com.api.formSync.repository.FormRepository;
import com.api.formSync.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepo;
    private final EmailService emailService;
    private final UserInfoService userInfoService;
    private final ApiKeyService apiKeyService;

    public FormResponse submit(FormRequest req, ApiKeyPrincipal details) {
        User user = userInfoService.loadWithFormsAndKey(details.getUser().getId());
        Form form = formRepo.save(new Form(req.getName(), req.getSubject(), req.getEmail(), req.getMessage()));

        user.getForms().add(formRepo.save(form));

        Role role = user.getRole();

        if (role.equals(Role.USER)) {
            ApiKey apiKey = user.getKey();
            apiKey.setRequestCount(apiKey.getRequestCount() + 1);
            apiKey.setLocked(apiKey.getRequestCount() >= 10);
            user.setKey(apiKeyService.update(apiKey));
        }

        userInfoService.update(user);

        emailService.sendFormEmail(user.getEmail(), new FormResponse(form));
        return new FormResponse(form);
    }
}