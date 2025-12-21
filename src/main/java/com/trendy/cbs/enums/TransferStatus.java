package com.trendy.cbs.enums;

public enum TransferStatus {
    PENDING,        // Created, not yet processed
    PROCESSING,     // Ledger entries being created
    SUCCESS,        // Completed successfully
    FAILED,         // Failed permanently
    REVERSED        // Successfully reversed
}
