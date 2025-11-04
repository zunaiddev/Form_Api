package com.api.formSync.Dto;

import com.api.formSync.model.User;
import com.api.formSync.util.Role;
import lombok.Data;

import java.time.Instant;

@Data
public class UserInfo {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Instant createdAt;

    public UserInfo(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }
}