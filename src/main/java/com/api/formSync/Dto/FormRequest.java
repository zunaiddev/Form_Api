package com.api.formSync.Dto;

import com.api.formSync.util.TextFormatter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormRequest {
    @NotBlank(message = "name is null or blank")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$",
            message = "name must contain only letters, spaces, hyphens, or apostrophes")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @JsonDeserialize(using = TextFormatter.class)
    private String name;

    @NotBlank(message = "email is null or blank")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "email must be a valid format (e.g., user@example.com)")
    @JsonDeserialize(using = TextFormatter.class)
    private String email;

    @NotBlank(message = "subject can't be black or null")
    @Size(min = 2, max = 100, message = "subject must be between 2 and 100 characters")
    @JsonDeserialize(using = TextFormatter.class)
    private String subject;

    @NotBlank(message = "message can't be black or null")
    @Size(min = 15, max = 200, message = "message must be between 15 and 200 characters")
    @JsonDeserialize(using = TextFormatter.class)
    private String message;
}
