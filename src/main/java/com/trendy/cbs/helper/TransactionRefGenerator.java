package com.trendy.cbs.helper;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@NoArgsConstructor
@Component
public class TransactionRefGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
