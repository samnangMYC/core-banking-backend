package com.trendy.cbs.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReverseFundTransferReq {
    @NotNull(message = "originalTransferId is required")
    private Long originalTransferId;

    @Size(min = 3, max = 200, message = "reason must be between 3 and 200 characters")
    private String reason;
}
