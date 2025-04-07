package com.api.formSync.model;

import com.api.formSync.util.Role;
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

    @Column(name = "last_reset", nullable = false)
    private LocalDate lastReset = LocalDate.now();

    @NotNull
    private Role role;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_id")
    private List<Domain> domains;

    @NotNull
    private boolean locked = false;

    @NotNull
    private boolean enabled = true;

    public ApiKey(User user, Domain domain) {
        this.user = user;
        this.apiKey = generate();
        this.role = user.getRole();
        this.domains = List.of(domain);
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

    public void addDomain(Domain domain) {
        this.domains.add(domain);
    }

    public void removeDomain(Domain domain) {
        this.domains.remove(domain);
    }
}