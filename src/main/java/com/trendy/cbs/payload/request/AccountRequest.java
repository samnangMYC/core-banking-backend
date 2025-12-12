package com.trendy.cbs.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
    private Long accTypeId;
    private Long cusId;
    private Long currencyId;
}
