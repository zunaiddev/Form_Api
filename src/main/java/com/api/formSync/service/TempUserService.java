package com.api.formSync.service;

import com.api.formSync.dto.SignupRequest;
import com.api.formSync.model.TempUser;
import com.api.formSync.repository.TempUserRepository;
import com.api.formSync.util.Log;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TempUserService {
    private final TempUserRepository repo;
    private final PasswordEncoder encoder;

    public TempUser create(SignupRequest req) {
        String encodedPassword = encoder.encode(req.getPassword());
        TempUser user = repo.findByEmail(req.getEmail()).orElse(null);

        if (user == null) {
            user = new TempUser(req.getName(), req.getEmail(), encodedPassword);
            Log.green("new user", user.toString());
            return repo.save(user);
        }

        if (user.getName().equals(req.getName()) && encoder.matches(req.getPassword(), user.getPassword())) {
            Log.green("Same as previous", user.toString());
            return user;
        }

        user.setName(req.getName());
        user.setPassword(req.getPassword());
        Log.green("Updates Something user", user.toString());

        return repo.save(user);
    }

    public void delete(TempUser user) {
        repo.delete(user);
    }
}