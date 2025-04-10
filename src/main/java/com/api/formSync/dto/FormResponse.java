package com.api.formSync.dto;

import com.api.formSync.model.Form;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "dd-mm-yy hh:mm a")
    private LocalDateTime submittedAt;

    public FormResponse(Form form) {
        this.id = form.getId();
        this.name = form.getName();
        this.email = form.getEmail();
        this.subject = form.getSubject();
        this.message = form.getMessage();
        this.submittedAt = form.getSubmittedAt();
    }
}
