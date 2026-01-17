package com.trendy.cbs.payload.dto;
import com.trendy.cbs.entity.Currency;
import com.trendy.cbs.enums.RateType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class ExchangeRateDTO {

    private Long id;

    private BigDecimal value;

    private CurrencyDTO fromCurrency;

    private CurrencyDTO toCurrency;

    private RateType rateType;

    private String source;

    private OffsetDateTime createdAt;
}
