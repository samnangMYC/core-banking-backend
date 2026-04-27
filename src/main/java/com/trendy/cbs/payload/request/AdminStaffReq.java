package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.Gender;
import com.trendy.cbs.enums.SystemRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminStaffReq {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username contains invalid characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
            message = "Password must contain uppercase, lowercase and number"
    )
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    private Gender gender;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Size(max = 100)
    private String nationality;

    @Size(max = 50)
    private String maritalStatus;

    @NotNull(message = "Staff role is required")
    private SystemRole systemRole;

    @Size(max = 100)
    private String department;

    @Size(max = 100)
    private String jobTitle;

    @PastOrPresent(message = "Hire date must be in the past or today")
    private LocalDate hireDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
    @Digits(integer = 15, fraction = 2)
    private BigDecimal salary;

}
