package com.trendy.cbs.entity;

import com.trendy.cbs.enums.BranchStatus;
import com.trendy.cbs.enums.BranchType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "branch")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchId;

    @Column(nullable = false)
    private String branchCode; // need to generate

    @Column(nullable = false)
    private String branchName;

    @Enumerated(EnumType.STRING)
    private BranchType branchType;

    @Enumerated(EnumType.STRING)
    private BranchStatus status;

    private LocalDate openedDate; // set date

    private LocalDate closedDate; // set to null

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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
