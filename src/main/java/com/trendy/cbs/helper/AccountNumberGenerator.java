package com.trendy.cbs.helper;

import com.trendy.cbs.repos.AccountRepository;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@NoArgsConstructor
public class AccountNumberGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_LENGTH = 12;

    /**
     * Generates a random numeric ID of specified length
     */
    public static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Generates a unique account number by checking the repository
     */
    public static String generateUniqueAccountNumber(int length, AccountRepository accountRepository) {
        String accountNumber;
        int maxAttempts = 10;
        int attempt = 0;

        do {
            accountNumber = generateRandomNumber(length);
            attempt++;
            if (attempt > maxAttempts) {
                throw new RuntimeException("Unable to generate unique account number after " + maxAttempts + " attempts");
            }
        } while (accountRepository.existsByAccNumber(accountNumber));

        return accountNumber;
    }

    /**
     * Default length version
     */
    public static String generateUniqueAccountNumber(AccountRepository accountRepository) {
        return generateUniqueAccountNumber(DEFAULT_LENGTH, accountRepository);
    }

}
