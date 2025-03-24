package com.api.formSync.service;

import com.api.formSync.dto.FormResponse;
import com.api.formSync.dto.UserInfo;
import com.api.formSync.exception.ResourceNotFoundException;
import com.api.formSync.model.User;
import com.api.formSync.repository.ApiKeyRepository;
import com.api.formSync.repository.FormRepository;
import com.api.formSync.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private UserRepository userRepo;
    private FormRepository formRepo;
    private ApiKeyRepository apiKeyService;

    public List<UserInfo> getUsers() {
        return userRepo.findAll()
                .stream()
                .map(UserInfo::new)
                .toList();
    }

    public UserInfo getUser(Long id) {
        return new UserInfo(userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Invalid user id")));
    }

    public UserInfo getUser(String email) {
        return new UserInfo(userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Invalid user email")));
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public List<FormResponse> getForms() {
        return formRepo.findAll().stream()
                .map(FormResponse::new).toList();
    }

    public List<FormResponse> getFormsByUserId(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found."));
        return formRepo.findAllByUser(user).stream()
                .map(FormResponse::new).toList();
    }

    public FormResponse getForm(Long id) {
        return new FormResponse(formRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Form not found.")));
    }

    public void deleteForm(Long id) {
        formRepo.deleteById(id);
    }
}
