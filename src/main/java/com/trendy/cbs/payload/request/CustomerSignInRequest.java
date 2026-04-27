package com.trendy.cbs.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerSignInRequest {

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{8,15}$")
    private String phoneNumber;

    @NotBlank
    @Size(min = 4, max = 6)
    private String passcode;
}
