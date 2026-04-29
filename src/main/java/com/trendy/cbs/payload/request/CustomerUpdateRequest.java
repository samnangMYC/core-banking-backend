package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerUpdateRequest {
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Last name must contain only letters")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Size(max = 100, message = "Occupation must not exceed 100 characters")
    private String occupation;

    @Size(max = 50, message = "Nationality must not exceed 50 characters")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Nationality must contain only letters")
    private String nationality;

    @Size(max = 20, message = "Marital status must not exceed 20 characters")
    @Pattern(
            regexp = "^(SINGLE|MARRIED|DIVORCED|WIDOWED)?$",
            message = "Marital status must be SINGLE, MARRIED, DIVORCED, or WIDOWED"
    )
    private String maritalStatus;

}
