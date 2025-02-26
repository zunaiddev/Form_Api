package com.api.formSync.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

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

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Column(name = "request_count", nullable = false)
    private Integer requestCount = 0;

    @Column(name = "last_used", nullable = false)
    private LocalDate lastReset = LocalDate.now();

    public ApiKey(User user, String apiKey) {
        this.user = user;
        this.apiKey = apiKey;
    }
}
