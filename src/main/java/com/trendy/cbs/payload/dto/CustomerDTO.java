package com.trendy.cbs.payload.dto;
import com.trendy.cbs.enums.CustomerStatus;
import com.trendy.cbs.enums.CustomerVerification;
import com.trendy.cbs.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CustomerDTO {
    private String customerId;

    private String username;

    // Business Status
    private CustomerStatus status;

    // Personal Info
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String phoneNumber;

    private String occupation;
    private String nationality;
    private String maritalStatus;

    // Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
