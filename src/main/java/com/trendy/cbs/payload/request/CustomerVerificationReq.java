package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.CustomerVerification;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerVerificationReq {
    @NotNull(message = "Verification must be provided")
    private CustomerVerification verification;
}
