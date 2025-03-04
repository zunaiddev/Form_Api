package com.api.formSync.service;

import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.exception.NoUserFoundException;
import com.api.formSync.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) {
        System.out.println("user details service entered");

        return repo.findByEmail(email)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new NoUserFoundException("Invalid username or password"));
    }
}
