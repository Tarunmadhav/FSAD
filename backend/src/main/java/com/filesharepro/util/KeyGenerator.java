package com.filesharepro.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component
public class KeyGenerator {
    private static final String DIGITS = "0123456789";
    private static final int KEY_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    public String generateKey() {
        StringBuilder key = new StringBuilder(KEY_LENGTH);
        for (int i = 0; i < KEY_LENGTH; i++) {
            key.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return key.toString();
    }
}