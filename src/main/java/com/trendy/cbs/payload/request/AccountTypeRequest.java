package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.PurposeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @NotNull(message = "Account purpose is required")
    private PurposeType purposeType;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest rate cannot be negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Interest rate cannot exceed 100%")
    private BigDecimal interestRate;

}
