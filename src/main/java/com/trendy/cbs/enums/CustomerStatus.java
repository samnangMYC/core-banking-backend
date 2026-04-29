package com.trendy.cbs.enums;

public enum CustomerStatus {
    PENDING,        // Created but not fully approved (before KYC verified)
    ACTIVE,         // Fully verified and allowed to use banking services
    SUSPENDED,      // Temporarily blocked (fraud, compliance, etc.)
    DORMANT,        // No activity for long period (optional but useful)
    CLOSED           // Permanently closed (cannot be used anymore)
}

