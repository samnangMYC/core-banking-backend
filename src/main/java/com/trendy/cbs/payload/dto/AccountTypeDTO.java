package com.trendy.cbs.payload.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.trendy.cbs.enums.PurposeType;
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
        "interestRate"
})
public class AccountTypeDTO {
    private Long id;

    private String code;

    private String name;

    private String description;

    private PurposeType purposeType;

    private BigDecimal interestRate;

}
