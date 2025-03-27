package com.api.formSync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "used_tokens")
public class UsedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 400, nullable = false)
    private String token;

    @NotNull
    private LocalDateTime expiry;

    public UsedToken(String token) {
        this.token = token;
        this.expiry = LocalDateTime.now();
    }
}
