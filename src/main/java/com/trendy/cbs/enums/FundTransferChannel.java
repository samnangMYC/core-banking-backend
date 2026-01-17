package com.trendy.cbs.enums;

import lombok.Getter;

/**
 * Enum representing the various channels or mechanisms
 * through which a fund transfer between accounts can be executed.
 *
 * <p>Each value corresponds to a specific transfer protocol or method,
 * used in the banking system to route and settle fund transfers.</p>
 *
 * <ul>
 *   <li>{@link #NEFT} - National Electronic Funds Transfer (batch-based)</li>
 *   <li>{@link #RTGS} - Real-Time Gross Settlement (high-value, instant)</li>
 *   <li>{@link #IMPS} - Immediate Payment Service (instant, available 24/7)</li>
 *   <li>{@link #UPI} - Unified Payments Interface (real-time, mobile-based payments)</li>
 *   <li>{@link #CARD_TO_CARD} - Transfer between debit or credit cards</li>
 *   <li>{@link #BRANCH} - Fund transfer initiated at a bank branch</li>
 * </ul>
 *
 * <p>Note: This enum represents the transfer mechanism,
 * not the medium or device used to initiate the transfer.
 * For example, transfers via a mobile app may still use IMPS or NEFT as the underlying mechanism.</p>
 */
@Getter
public enum FundTransferChannel {
    NEFT(5.0, 0.0),
    RTGS(25.0, 0.1),
    IMPS(2.0, 0.0),
    UPI(0.0, 0.0),
    CARD_TO_CARD(10.0, 0.5),
    BRANCH(15.0, 0.0),
    SWIFT(50.0, 0.5);

    private final double fixedFee;
    private final double percentageFee;

    FundTransferChannel(double fixedFee, double percentageFee) {
        this.fixedFee = fixedFee;
        this.percentageFee = percentageFee;
    }
}
