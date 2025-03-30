package com.api.formSync.Service;

import com.api.formSync.Principal.UserPrincipal;
import com.api.formSync.exception.UserNotFoundException;
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
        return repo.findByEmail(email)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UserNotFoundException("Invalid username or password"));
    }
}
