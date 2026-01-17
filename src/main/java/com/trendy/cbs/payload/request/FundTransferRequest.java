package com.trendy.cbs.payload.request;

import com.trendy.cbs.entity.Currency;
import com.trendy.cbs.enums.FundTransferChannel;
import com.trendy.cbs.enums.TransferStatus;
import com.trendy.cbs.enums.TransferType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundTransferRequest {

    private Long fromAccountId;

    private Long toAccountId;

    private BigDecimal amount;

    private String purpose;

}
