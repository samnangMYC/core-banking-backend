package com.trendy.cbs.service.factory;

import com.trendy.cbs.entity.Account;
import com.trendy.cbs.entity.LedgerEntry;
import com.trendy.cbs.enums.Glcode;
import com.trendy.cbs.enums.LedgerEntryType;
import com.trendy.cbs.helper.LedgerEntryRefGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class LedgerEntryFactory{

    private final LedgerEntryRefGenerator referenceGenerator;
    OffsetDateTime utcTime = OffsetDateTime.now(ZoneOffset.UTC); // UTC timezone (equivalent to +00:00)

    public LedgerEntry credit(
            Account account,
            BigDecimal creditAmount,
            BigDecimal runningBalance,
            LedgerEntryType ledgerEntryType,
            String description,
            Glcode glcode
    ) {
        return LedgerEntry.builder()
                .account(account)
                .transactionReference(referenceGenerator.generate())
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(creditAmount)
                .runningBalance(runningBalance)
                .type(ledgerEntryType)
                .description(description)
                .postedAt(utcTime)
                .glCode(glcode)
                .build();
    }

    public LedgerEntry debit(
            Account account,
            BigDecimal debitAmount,
            BigDecimal runningBalance,
            LedgerEntryType ledgerEntryType,
            String description,
            Glcode glcode
    ) {
        return LedgerEntry.builder()
                .account(account)
                .transactionReference(referenceGenerator.generate())
                .debitAmount(debitAmount)
                .creditAmount(BigDecimal.ZERO)
                .runningBalance(runningBalance)
                .type(ledgerEntryType)
                .description(description)
                .postedAt(utcTime)
                .glCode(glcode)
                .build();
    }


}
