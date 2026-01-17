package com.trendy.cbs.payload.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CurrencyDTO {

    private Long curId;

    private String code;

    private String symbol;

    private Integer decimalPlaces;

    private String name;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
