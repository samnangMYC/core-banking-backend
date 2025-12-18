package com.trendy.cbs.payload.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DepositReq {

    @NotNull(message = "Deposit amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Deposit amount must be greater than zero")
    private BigDecimal amount;
}
