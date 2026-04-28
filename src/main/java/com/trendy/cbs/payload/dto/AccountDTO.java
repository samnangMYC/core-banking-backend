package com.trendy.cbs.payload.dto;

import com.trendy.cbs.enums.AccountStatus;
import com.trendy.cbs.enums.OwnershipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {

    private Long accId;

    private String accNumber;

    private BigDecimal balance;

    private BigDecimal lienAmount;

    private AccountStatus status;

    private OwnershipType ownershipType;

    private AccountTypeSummaryDTO accountType;

    private CustomerSummaryDTO customer;

    private CurrencySummaryDTO currency;

    private LocalDateTime openedAt;

    private LocalDateTime closedAt;

    private LocalDateTime updatedAt;

}
