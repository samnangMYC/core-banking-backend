package com.trendy.cbs.entity;

import com.trendy.cbs.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * Represents a bank account entity in the system.
 * <p>
 * This entity maps to the {@code accounts} table in the database and contains
 * information about account details, balance, status, account type, associated user, and currency.
 * </p>
 *
 * <p>
 * Relationships:
 * <ul>
 *     <li>{@link AccountType} - One-to-One relationship representing the type of the account.</li>
 *     <li>{@link User} - Many-to-One relationship representing the account owner.</li>
 *     <li>{@link Currency} - One-to-One relationship representing the currency of the account.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Timestamps for creation and last update are automatically managed using {@link CreationTimestamp} and {@link UpdateTimestamp}.
 * </p>
 *
 * @author samnang
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
@Builder
public class Account {

    /**
     * Unique identifier for the account.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accId;

    /**
     * Account number assigned to this account.
     */
    @Column(unique = true)
    private Long accNumber;

    /**
     * Name of the account holder or account title.
     */
    private String name;

    /**
     * Current balance of the account.
     */
    private BigDecimal balance;

    /**
     * Amount in lien that cannot be used until released.
     */
    private BigDecimal lienAmount;

    /**
     * Current status of the account.
     * <p>Values are defined in {@link AccountStatus} enum.</p>
     */
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    /**
     * Timestamp when the account was created.
     * Managed automatically by Hibernate.
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Timestamp when the account was last updated.
     * Managed automatically by Hibernate.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Type of the account.
     * <p>One-to-One relationship with {@link AccountType}.</p>
     */
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "account_type_id", unique = true)
    private AccountType accountType;

    /**
     * The customer who owns this account.
     * <p>Many-to-One relationship with {@link Customer}.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cus_id", nullable = false)
    private Customer customer;

    /**
     * Currency of the account.
     * <p>One-to-One relationship with {@link Currency}.</p>
     */
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "currency_id")
    private Currency currency;



}
