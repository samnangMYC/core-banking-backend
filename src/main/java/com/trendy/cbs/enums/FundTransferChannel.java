package com.trendy.cbs.enums;
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
public enum FundTransferChannel {
    NEFT,
    RTGS,
    IMPS,
    UPI,
    CARD_TO_CARD,
    BRANCH
}
