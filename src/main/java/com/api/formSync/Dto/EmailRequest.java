package com.api.formSync.Dto;

import com.api.formSync.util.TextFormatter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailRequest {
    @NotBlank(message = "Email is null or blank")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email must be a valid format (e.g., user@example.com)")
    @JsonDeserialize(using = TextFormatter.class)
    private String email;
}
