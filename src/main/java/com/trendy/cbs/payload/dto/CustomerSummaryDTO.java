package com.trendy.cbs.payload.dto;

import com.trendy.cbs.enums.CustomerStatus;
import com.trendy.cbs.enums.CustomerVerification;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CustomerSummaryDTO {
    private String fullName;
    private String phoneNumber;
    private CustomerStatus status;
    private CustomerVerification verification;
}
