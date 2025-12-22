package com.trendy.cbs.helper;

import com.trendy.cbs.repos.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LedgerEntryRefGenerator {

    private final LedgerEntryRepository ledgerEntryRepository;

    public String generate() {
        return  ledgerEntryRepository.next();
    }
}
