package com.api.formSync;

import com.api.formSync.util.Log;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;


class FormSyncApplicationTests {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Test
    void contextLoads() {
        String normal = UUID.randomUUID().toString();
        String secured = generateKey();
        Log.blue("UUID KEY: ", normal);
        Log.blue("Encoded UUID:", encoder.encode(normal));
        Log.blue("Secured:", secured);
        Log.blue("Secured Encoded:", encoder.encode(secured));
    }

    private String generateKey() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return base64Encoder.encodeToString(bytes);
    }

}
