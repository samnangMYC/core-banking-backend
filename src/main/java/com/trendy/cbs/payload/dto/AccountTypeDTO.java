package com.trendy.cbs.payload.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonPropertyOrder({
        "id",
        "code",
        "name",
        "description",
        "interestRate",
        "maxTranBalanceDaily",
        "atmLimitedTranDaily",
        "fee"
})
public class AccountTypeDTO {
    private Long id;

    private String code;

    private String name;

    private String description;

    private BigDecimal interestRate;

    private BigDecimal maxTranBalanceDaily;

    private BigDecimal atmLimitedTranDaily;

    private BigDecimal fee;
}
