package com.api.formSync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@Entity
@Table(name = "forms")
@NoArgsConstructor
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String subject;

    @NotNull
    private String message;

    @CreationTimestamp
    private Instant submittedAt;

    public Form(String name, String subject, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
        this.subject = subject;
    }
}
