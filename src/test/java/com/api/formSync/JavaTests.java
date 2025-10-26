package com.api.formSync;

import com.api.formSync.util.Purpose;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class JavaTests {

    @Test
    void test() {
        Set<Purpose> authPurposes = Set.of(Purpose.VERIFY_USER, Purpose.RESET_PASSWORD,
                Purpose.REACTIVATE_USER, Purpose.UPDATE_EMAIL);

        assertFalse(authPurposes.contains(null));
    }
}
