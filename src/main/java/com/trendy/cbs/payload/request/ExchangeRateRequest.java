package com.trendy.cbs.payload.request;

import com.trendy.cbs.entity.Currency;
import com.trendy.cbs.enums.RateType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRateRequest {
    @NotNull(message = "Exchange rate value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Exchange rate must be positive")
    private BigDecimal value;

    @NotNull(message = "From currency ID is required")
    private Long fromCurrencyId;

    @NotNull(message = "To currency ID is required")
    private Long toCurrencyId;

    @NotNull(message = "Rate type is required")
    @Enumerated(EnumType.STRING)
    private RateType rateType;

    @NotBlank(message = "Source is required")
    private String source;
}
