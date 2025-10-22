package com.api.formSync.Service;

import com.api.formSync.model.User;
import com.api.formSync.util.Purpose;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class GenerateTokenService {
    private final JwtService jwtService;

    public String accessToken(User user) {
        return jwtService.generateToken(user.getId(),
                Map.of("email", user.getEmail(), "role", user.getRole(),
                        "purpose", Purpose.AUTH), 900);
    }

    public String refreshToken(User user) {
        return jwtService.generateToken(user.getId(),
                Map.of("purpose", Purpose.REFRESH_TOKEN), 2_592_000);
    }

    public String resetPassword(User user) {
        return jwtService.generateToken(user.getId(),
                Map.of("email", user.getEmail(), "role", user.getRole(), "purpose", Purpose.RESET_PASSWORD), 900);
    }

    public String verifyUser(User user) {
        return jwtService.generateToken(user.getId(),
                Map.of("email", user.getEmail(),
                        "role", user.getRole(),
                        "purpose", Purpose.VERIFY_USER), 900);
    }
}
