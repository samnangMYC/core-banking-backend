package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.OwnershipType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountReq {

    @NotNull(message = "Account type is required")
    private Long accountTypeId;

    @NotNull(message = "Currency is required")
    private Long currencyId;

    private OwnershipType ownershipType;

}
