package com.trendy.cbs.enums;

import lombok.Getter;

@Getter
public enum Glcode {
    CASH("1000", "Cash on Hand"),  // Asset
    DEPOSITS("2000", "Customer Deposits"),  // Liability
    LOANS("3000", "Loans Receivable"),  // Asset (for Loan Module integration)
    INTEREST_INCOME("4000", "Interest Income"),  // Revenue
    FEE_INCOME("5000", "Fee Income"),  // Revenue
    INTEREST_EXPENSE("6000", "Interest Expense"),  // Expense
    FEE_EXPENSE("7000", "Fee Expense");  // Expense

    private final String code;
    private final String description;

    Glcode(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
