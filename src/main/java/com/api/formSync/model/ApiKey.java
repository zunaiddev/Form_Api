package com.api.formSync.model;

import com.api.formSync.util.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;

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

    @Column(name = "request_count", nullable = false)
    private Integer requestCount;

    @Column(name = "last_reset", nullable = false)
    private Instant lastReset;

    @NotNull(message = "Role cannot be null")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @NotNull
    private boolean locked;

    @NotNull
    private boolean enabled;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private User user;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @ToString.Exclude
    private Set<Domain> domains;

    public ApiKey(User user) {
        this.user = user;
        this.apiKey = generate();
        this.role = user.getRole();
        this.lastReset = Instant.now();
        this.requestCount = 0;
        this.locked = false;
        this.enabled = true;
    }

    public void reGenerate() {
        this.apiKey = generate();
    }

    private String generate() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[25];
        secureRandom.nextBytes(keyBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes)
                .replaceAll("[^a-zA-Z0-9 ]", "");
    }
}