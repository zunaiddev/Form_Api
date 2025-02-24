package com.api.formSync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tokens")
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private LocalDateTime expiry;

    public Token(User user) {
        this.user = user;
        this.expiry = LocalDateTime.now().plusHours(12);
        this.token = UUID.randomUUID().toString();
    }

    public void regenerate() {
        this.expiry = LocalDateTime.now().plusHours(12);
        this.token = UUID.randomUUID().toString();
    }
}