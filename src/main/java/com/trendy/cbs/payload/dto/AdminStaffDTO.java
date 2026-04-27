package com.trendy.cbs.payload.dto;

import com.trendy.cbs.enums.EmploymentStatus;
import com.trendy.cbs.enums.SystemRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminStaffDTO {
    private Long userId;
    private Long staffId;

    private String keycloakUserId;
    private String username;
    private String email;

    private String staffCode;
    private String firstName;
    private String lastName;

    private SystemRole systemRole;
    private EmploymentStatus employmentStatus;

    private String department;
    private String jobTitle;

    private LocalDateTime createdAt;
}
