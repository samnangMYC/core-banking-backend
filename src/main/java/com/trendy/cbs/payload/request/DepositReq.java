package com.trendy.cbs.payload.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositReq {

    @NotNull(message = "Deposit amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Deposit amount must be greater than zero")
    @Digits(integer = 18, fraction = 4, message = "Amount must have up to 18 digits and 4 decimal places")
    private BigDecimal amount;


}
