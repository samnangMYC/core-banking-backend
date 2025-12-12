package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.BranchType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchRequest {

    @NotBlank(message = "Branch name is required.")
    @Size(max = 100, message = "Branch name cannot exceed 100 characters.")
    private String branchName;

    @NotNull(message = "Branch type is required.")
    private BranchType branchType;

    @NotBlank(message = "Address line 1 is required.")
    @Size(max = 150, message = "Address line 1 cannot exceed 150 characters.")
    private String line1;

    @Size(max = 150, message = "Address line 2 cannot exceed 150 characters.")
    private String line2;

    @NotBlank(message = "City is required.")
    @Size(max = 100, message = "City cannot exceed 100 characters.")
    private String city;

    @NotBlank(message = "State is required.")
    @Size(max = 100, message = "State cannot exceed 100 characters.")
    private String state;

    @NotBlank(message = "Country is required.")
    @Size(max = 100, message = "Country cannot exceed 100 characters.")
    private String country;

    @NotBlank(message = "Postal code is required.")
    @Pattern(
            regexp = "^[A-Za-z0-9\\- ]{3,12}$",
            message = "Postal code must be alphanumeric and between 3–12 characters."
    )
    private String postalCode;

    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = "^[0-9+()\\- ]{7,20}$",
            message = "Phone number contains invalid characters."
    )
    private String phoneNumber;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    @Size(max = 150, message = "Email cannot exceed 150 characters.")
    private String email;

    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90.")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90.")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180.")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180.")
    private BigDecimal longitude;

    @NotBlank(message = "IFSC code is required.")
    @Pattern(
            regexp = "^[A-Za-z]{4}[0][A-Za-z0-9]{6}$",
            message = "Invalid IFSC code format."
    )
    private String ifscCode;

    @Size(max = 11, message = "SWIFT code cannot exceed 11 characters.")
    @Pattern(
            regexp = "^[A-Za-z0-9]{8}(?:[A-Za-z0-9]{3})?$",
            message = "Invalid SWIFT code format."
    )
    private String swiftCode;

}
