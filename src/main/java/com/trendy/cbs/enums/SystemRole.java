package com.trendy.cbs.enums;

import lombok.Getter;

/**
 * Defines the hierarchical and functional roles within the Core Banking System.
 * These roles are used to enforce Segregation of Duties (SoD) and Access Control.
 */
@Getter
public enum SystemRole {

    /**
     * Handles front-line cash transactions, deposits, and withdrawals.
     */
    TELLER,

    /**
     * External client with restricted access to personal financial data only.
     */
    CUSTOMER,

    /**
     * Manages General Ledger (GL) entries and financial reconciliation.
     */
    ACCOUNTANT,

    /**
     * Internal or external oversight with read-only access for compliance checks.
     */
    AUDITOR,

    /**
     * Provides first-level authorization for transactions exceeding Teller limits.
     */
    SUPERVISOR,

    /**
     * Oversees department-specific operations and staff performance.
     */
    MANAGER,

    /**
     * Highest administrative authority at the branch level for overrides and approvals.
     */
    BRANCH_MANAGER,

    /**
     * Handles back-office batch processing, clearing, and EOD (End of Day) tasks.
     */
    OPERATIONS,

    /**
     * Manages system configuration, security settings, and user provisioning.
     */
    SYSTEM_ADMIN
}