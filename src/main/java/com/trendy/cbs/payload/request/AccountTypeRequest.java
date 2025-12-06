package com.trendy.cbs.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountTypeRequest {

    @NotBlank(message = "Code is required")
    @Pattern(regexp = "^[A-Z0-9]{3,10}$", message = "Code must be 3-10 uppercase letters or numbers")
    private String code;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest rate cannot be negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Interest rate cannot exceed 100%")
    private BigDecimal interestRate;

    @NotNull(message = "Max transaction balance daily is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Max transaction balance daily cannot be negative")
    private BigDecimal maxTranBalanceDaily;

    @NotNull(message = "ATM limited transaction daily is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "ATM limited transaction daily cannot be negative")
    private BigDecimal atmLimitedTranDaily;

    @NotNull(message = "Fee is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Fee cannot be negative")
    private BigDecimal fee;
}
