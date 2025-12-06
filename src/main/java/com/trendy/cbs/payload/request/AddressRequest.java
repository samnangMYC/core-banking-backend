package com.trendy.cbs.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {

    @NotBlank(message = "Line 1 is required")
    @Size(max = 150, message = "Line 1 must be less than 150 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9 ,.\\-#/]+$",
            message = "Line 1 contains invalid characters"
    )
    private String line1;

    @Size(max = 150, message = "Line 2 must be less than 150 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9 ,.\\-#/]*$",
            message = "Line 2 contains invalid characters"
    )
    private String line2;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must be less than 100 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9 .\\-]+$",
            message = "City contains invalid characters"
    )
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State must be less than 100 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9 .\\-]+$",
            message = "State contains invalid characters"
    )
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must be less than 100 characters")
    @Pattern(
            regexp = "^[A-Za-z .\\-]+$",
            message = "Country contains invalid characters"
    )
    private String country;

}
