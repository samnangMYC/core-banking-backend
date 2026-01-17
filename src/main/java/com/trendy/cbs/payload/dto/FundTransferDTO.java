package com.trendy.cbs.payload.dto;

import com.trendy.cbs.entity.Currency;
import com.trendy.cbs.enums.FundTransferChannel;
import com.trendy.cbs.enums.TransferStatus;
import com.trendy.cbs.enums.TransferType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundTransferDTO {

    private Long id;

    private String transactionReference;

    private String externalReference;

    private Long fromAccountId;

    private Long toAccountId;

    private BigDecimal amount;

    private CurrencySummaryDTO currency;

    private String purpose;

    private TransferType transferType;

    private TransferStatus status;

    private FundTransferChannel channel;

    private OffsetDateTime initiatedAt;

    private OffsetDateTime processedAt;

    private OffsetDateTime completedAt;

    private Boolean reversed;

    private String reversalReference;

    private String failedReason;

}