package com.api.formSync.Controller;

import com.api.formSync.Dto.FormRequest;
import com.api.formSync.Dto.FormResponse;
import com.api.formSync.Principal.ApiKeyPrincipal;
import com.api.formSync.Service.FormService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@AllArgsConstructor
public class FormController {
    private final FormService service;

    @PostMapping("/submit")
    public FormResponse submit(@AuthenticationPrincipal ApiKeyPrincipal details,
                                           @Valid @RequestBody FormRequest req) {
        return service.submit(req, details);
    }
}