package com.api.formSync.model;

import jakarta.persistence.*;
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

    @Column(length = 500, unique = true, nullable = false)
    private String token;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    public UsedToken(String token) {
        this.token = token;
    }
}