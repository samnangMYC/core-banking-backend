package com.trendy.cbs.payload.dto;

import com.trendy.cbs.enums.BranchStatus;
import com.trendy.cbs.enums.BranchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDTO {

    private Long branchId;

    private String branchCode;

    private String branchName;

    private BranchType branchType;

    private BranchStatus status;

    private LocalDate openedDate;

    private LocalDate closedDate;

    private String line1;

    private String line2;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private String phoneNumber;

    private String email;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String ifscCode;

    private String swiftCode;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
