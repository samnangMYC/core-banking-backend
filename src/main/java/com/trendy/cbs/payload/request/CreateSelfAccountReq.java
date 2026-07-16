package com.trendy.cbs.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trendy.cbs.enums.OwnershipType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSelfAccountReq {

    @NotNull(message = "Account type is required")
    private Long accountTypeId;

    @NotNull(message = "Currency is required")
    private Long currencyId;


}
