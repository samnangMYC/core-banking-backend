package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRegistrationRequest {

    // Personal Info
    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    // Contact Info
    @Email(message = "Invalid email format")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+?[0-9]{8,15}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber;

    @NotBlank
    @Size(min = 4, max = 6)
    private String passcode;

    // Additional Info
    @Size(max = 100)
    private String occupation;

    @Size(max = 100)
    private String nationality;

    @Size(max = 50)
    private String maritalStatus;


}
