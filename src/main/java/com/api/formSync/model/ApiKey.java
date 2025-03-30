package com.api.formSync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Data
@Entity
@Table(name = "api_keys")
@NoArgsConstructor
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String apiKey;

    @OneToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Column(name = "request_count", nullable = false)
    private Integer requestCount = 0;

    @Column(name = "last_used", nullable = false)
    private LocalDate lastReset = LocalDate.now();

    @NotNull
    private List<String> domains;

    public ApiKey(User user, List<String> domains) {
        this.user = user;
        this.apiKey = generate();
        this.domains = domains;
    }

    public void reGenerate() {
        this.apiKey = generate();
    }

    private String generate() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[24];
        secureRandom.nextBytes(keyBytes);

        String apiKey = Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);
        return apiKey.toUpperCase().replaceAll("(.{4})", "$1-").substring(0, 35);
    }
}
