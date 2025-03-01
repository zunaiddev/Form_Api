package com.api.formSync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "token")
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(optional = false)
    @JoinColumn(name = "tempUser_id")
    private TempUser user;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime expiry;

    public Token(TempUser user) {
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.expiry = LocalDateTime.now().plusMinutes(15);
        this.token = UUID.randomUUID().toString().replace("-", "_");
    }

    public void regenerate() {
        this.createdAt = LocalDateTime.now();
        this.expiry = LocalDateTime.now().plusMinutes(15);
        this.token = UUID.randomUUID().toString();
    }
}