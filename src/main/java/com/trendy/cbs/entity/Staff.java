package com.trendy.cbs.entity;

import com.trendy.cbs.enums.EmploymentStatus;
import com.trendy.cbs.enums.Gender;
import com.trendy.cbs.enums.StaffRole;
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
@Table(
        name = "staffs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_staff_user_id", columnNames = "user_id"),
                @UniqueConstraint(name = "uk_staff_code", columnNames = "staff_code"),
                @UniqueConstraint(name = "uk_staff_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_staff_phone", columnNames = "phone_number")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Internal staff code like STF-0001
     */
    @Column(name = "staff_code", nullable = false, length = 50, unique = true)
    private String staffCode;

    /**
     * Identity relation from local user table
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * Basic personal info
     */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 30, unique = true)
    private String phoneNumber;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "marital_status", length = 50)
    private String maritalStatus;

    /**
     * Employment details
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "staff_role", nullable = false, length = 50)
    private StaffRole staffRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status", nullable = false, length = 30)
    private EmploymentStatus employmentStatus;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Column(name = "salary", precision = 19, scale = 4)
    private BigDecimal salary;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
