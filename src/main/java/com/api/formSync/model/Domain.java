package com.api.formSync.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "domains")
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "domains", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @ToString.Exclude
    private Set<ApiKey> apiKeys;

    public Domain(String name) {
        this.name = name;
    }
}