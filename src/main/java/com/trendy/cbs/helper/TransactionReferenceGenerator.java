package com.trendy.cbs.helper;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@NoArgsConstructor
public class TransactionReferenceGenerator {

    /**
     * Generates numeric transaction reference starting from 1000
     * Example: 1000, 1001, 1002...
     */
    public String generate() {
        int randomNum = (int) Math.floor(Math.random() * 1000);
        return String.valueOf(randomNum);
    }

    /**
     * Optional prefixed version
     * Example: TXN-1000
     */
    public String generateWithPrefix() {
        return "TXN-" + generate();
    }
}
