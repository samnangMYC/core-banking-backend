package com.trendy.cbs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "staff_addresses",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_staff_address_staff_id", columnNames = "staff_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house_no", length = 50)
    private String houseNo;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "commune", length = 100)
    private String commune;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "country", length = 100)
    private String country;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false, unique = true)
    private Staff staff;
}
