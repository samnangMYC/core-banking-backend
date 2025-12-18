package com.trendy.cbs.payload.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencySummaryDTO {

    private String code;

    private String name;

}
