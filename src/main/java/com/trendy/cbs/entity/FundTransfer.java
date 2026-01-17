package com.trendy.cbs.entity;

import com.trendy.cbs.enums.FundTransferChannel;
import com.trendy.cbs.enums.TransferStatus;
import com.trendy.cbs.enums.TransferType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "fund_transfers",
        indexes = {
                @Index(name = "idx_transfer_reference", columnList = "transaction_reference"),
                @Index(name = "idx_transfer_status", columnList = "status"),
                @Index(name = "idx_transfer_initiated_at", columnList = "initiated_at")
        }
)
@Builder
public class FundTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     Transaction Identity
    */
    @Column(name = "transaction_reference", nullable = false, unique = true, updatable = false)
    private String transactionReference; // auto generate by system

    @Column(name = "external_reference", length = 100)
    private String externalReference; // interbank or other gateway

    /**
     Parties Involved
     */
    @Column(name = "from_account_id", nullable = false, updatable = false)
    private Long fromAccountId;

    @Column(name = "to_account_id", nullable = false, updatable = false)
    private Long toAccountId;

    /**
     Financial Data
     */
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    /**
     Transfer Metadata
     */
    @Column(length = 150)
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransferType transferType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransferStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FundTransferChannel channel;

    /**
     Lifecycle Tracking
     */

    @Column(name = "initiated_at", nullable = false, updatable = false)
    private OffsetDateTime initiatedAt;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    /**
     Reversal Support
     */
    @Column(name = "reversed", nullable = false)
    private Boolean reversed = false;

    @Column(name = "reversal_reference")
    private String reversalReference;

    @Column(name = "failed_reason", length = 255)
    private String failedReason;

    @PrePersist
    protected void onCreate() {
        this.initiatedAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.status = TransferStatus.PENDING;
    }
}
