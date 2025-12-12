package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.CustomerDocStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerStatusRequest {

    @NotNull(message = "User status is required")
    private CustomerDocStatus docStatus;
}
