package com.trendy.cbs.payload.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.trendy.cbs.enums.Glcode;
import com.trendy.cbs.enums.LedgerEntryType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@JsonPropertyOrder({
        "id",
        "transactionReference",
        "debitAmount",
        "creditAmount",
        "runningBalance",
        "type",
        "description",
        "postedAt",
        "glCode",
        "accountId"
})
public class LedgerEntryDTO {

    private Long id;

    private String transactionReference;

    private BigDecimal debitAmount;

    private BigDecimal creditAmount;

    private BigDecimal runningBalance;

    private LedgerEntryType type;

    private String description;

    private OffsetDateTime postedAt;

    private Glcode glCode;

    private Long accountId;
}
