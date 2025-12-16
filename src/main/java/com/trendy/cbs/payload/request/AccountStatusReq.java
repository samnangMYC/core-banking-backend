package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountStatusReq {
    @NotNull(message = "Account status is required")
    public AccountStatus status;
}
