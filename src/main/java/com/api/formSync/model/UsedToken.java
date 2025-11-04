package com.api.formSync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "used_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String token;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    public UsedToken(String token) {
        this.token = token;
    }
}