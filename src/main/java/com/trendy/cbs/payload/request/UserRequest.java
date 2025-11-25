package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.Gender;
import com.trendy.cbs.enums.MaritalStatus;
import com.trendy.cbs.enums.UserStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "First name can only contain letters, spaces, hyphens, or apostrophes")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Last name can only contain letters, spaces, hyphens, or apostrophes")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be in international format (E.164)")
    private String phoneNumber;

    @Size(max = 100, message = "Occupation must be at most 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]*$", message = "Occupation can only contain alphanumeric characters, spaces, or hyphens")
    private String occupation;

    @NotNull(message = "Nationality is required")
    private String nationality;

    @NotNull(message = "Marital status is required")
    private MaritalStatus maritalStatus;

    @Pattern(regexp = "^(http|https)://[^\\s$.?#].[^\\s]*$", message = "Profile image must be a valid URL")
    @Size(max = 255, message = "Profile image URL must be at most 255 characters")
    private String profileImage;

    private UserStatus status;
}
