package com.api.formSync.dto;

import com.api.formSync.model.User;
import com.api.formSync.util.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfo {
    private Long id;
    private String name;
    private String email;
    private Role role;
    @JsonFormat(pattern = "dd-MMM-yyyy hh-mm", timezone = "Asia/Kolkata")
    private LocalDateTime createdAt;

    public UserInfo(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }
}