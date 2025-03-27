package com.api.formSync;

import com.api.formSync.Service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FormSyncApplicationTests {
    @Autowired
    TokenService service;

    @Test
    void test() {
        service.saveUsedToken("token1");
    }
}
