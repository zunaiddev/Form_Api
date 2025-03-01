package com.api.formSync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "temp_users")
@AllArgsConstructor
@NoArgsConstructor
public class TempUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

    public TempUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
