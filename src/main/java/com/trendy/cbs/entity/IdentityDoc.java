package com.trendy.cbs.entity;

import com.trendy.cbs.enums.DocStatus;
import com.trendy.cbs.enums.IdentityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
/**
 * Represents an identity document associated with a user.
 * This entity stores details such as document number, type, status,
 * expiration date, verification date, and the document image.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "identity_doc")
@Builder
public class IdentityDoc {

    /**
     * The unique identifier of the identity document.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docId;

    /**
     * The unique number of the identity document (e.g., passport or national ID number).
     */
    @Column(nullable = false, unique = true)
    private String number;

    /**
     * The expiration date of the document.
     */
    @Column(nullable = false)
    private LocalDateTime expDate;

    /**
     * The date when the document was verified.
     * Can be null if the document is not yet verified.
     */
    private LocalDateTime verifiedDate;

    /**
     * Path or URL of the document image.
     */
    private String docImage;

    /**
     * The current status of the document (e.g., PENDING, VERIFIED, REJECTED).
     */
    @Enumerated(EnumType.STRING)
    private DocStatus docStatus;

    /**
     * The type of identity document (e.g., PASSPORT, NID, DRIVER_LICENSE).
     */
    @Enumerated(EnumType.STRING)
    private IdentityType identityType;

    /**
     * The customer to whom this identity document belongs.
     * Each customer can have only one identity document.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cus_id",unique = true)
    private Customer customer;

}