package com.api.formSync.dto;

import com.api.formSync.model.User;
import com.api.formSync.util.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfo {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private ApiKeyDto keyInfo;

    public UserInfo(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        if (!(user.getKey() == null)) {
            this.keyInfo = new ApiKeyDto(user.getKey());
        }
        System.out.println("passed");
    }
}
