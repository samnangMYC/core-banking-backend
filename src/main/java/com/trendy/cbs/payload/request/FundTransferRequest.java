package com.trendy.cbs.payload.request;

import com.trendy.cbs.entity.Currency;
import com.trendy.cbs.enums.FundTransferChannel;
import com.trendy.cbs.enums.TransferStatus;
import com.trendy.cbs.enums.TransferType;
import jakarta.validation.constraints.*;
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

    @NotNull(message = "fromAccountId is required")
    private Long fromAccountId;

    @NotNull(message = "toAccountId is required")
    private Long toAccountId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "amount must be greater than 0")
    @Digits(integer = 16, fraction = 6, message = "amount format is invalid (max 16 digits and 6 decimals)")
    private BigDecimal amount;

    @NotBlank(message = "purpose is required")
    @Size(min = 3, max = 200, message = "purpose must be between 3 and 200 characters")
    private String purpose;

}
