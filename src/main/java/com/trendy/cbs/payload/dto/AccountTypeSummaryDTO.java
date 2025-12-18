package com.trendy.cbs.payload.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountTypeSummaryDTO {

    private String code;

    private String name;

    private BigDecimal interestRate;

}
