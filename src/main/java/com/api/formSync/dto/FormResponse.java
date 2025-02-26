package com.api.formSync.dto;

import com.api.formSync.model.Form;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FormResponse {
    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private LocalDateTime submittedAt;

    public FormResponse(Form form) {
        this.id = form.getId();
        this.name = form.getName();
        this.email = form.getEmail();
        this.subject = form.getSubject();
        this.message = form.getSubject();
        this.submittedAt = form.getSubmittedAt();
    }
}
