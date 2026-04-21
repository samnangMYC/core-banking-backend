package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Account;
import com.trendy.cbs.entity.Currency;
import com.trendy.cbs.entity.FundTransfer;
import com.trendy.cbs.entity.LedgerEntry;
import com.trendy.cbs.enums.*;
import com.trendy.cbs.exception.BusinessException;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.helper.TransactionRefGenerator;
import com.trendy.cbs.mapper.FundTransferMapper;
import com.trendy.cbs.payload.dto.FundTransferDTO;
import com.trendy.cbs.payload.request.FundTransferRequest;
import com.trendy.cbs.payload.request.ReverseFundTransferReq;
import com.trendy.cbs.repos.AccountRepository;
import com.trendy.cbs.repos.FundTransferRepository;
import com.trendy.cbs.repos.LedgerEntryRepository;
import com.trendy.cbs.service.FundTransferService;
import com.trendy.cbs.service.factory.LedgerEntryFactory;
import com.trendy.cbs.service.validation.AccountValidationService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FundTransferServiceImpl implements FundTransferService {

    private final AccountRepository accountRepository;
    private final AccountValidationService accountValidationService;
    private final LedgerEntryFactory ledgerEntryFactory;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final FundTransferMapper fundTransferMapper;
    private final FundTransferRepository fundTransferRepository;

    @Override
    @Transactional
    public FundTransferDTO createFundTransfer(FundTransferRequest req) {

        // Fetch accounts with lock to prevent race conditions
        Account senderAccount = accountRepository.findByIdForUpdate(req.getFromAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", req.getFromAccountId()));

        Account receiverAccount = accountRepository.findByIdForUpdate(req.getToAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", req.getToAccountId()));

        // Validate accounts status
        accountValidationService.validateAccountStatus(senderAccount);
        accountValidationService.validateAccountStatus(receiverAccount);

        // Internal transfer must match currency
        if (senderAccount.getCurrency() != receiverAccount.getCurrency()) {
            throw new BusinessException("Currency mismatch: internal transfer requires same currency.",
                    ErrorCode.INVALID_OPERATION, 400);
        }

        // Validate transfer amount (and normalize scale)
        BigDecimal amount = resolveAmount(req,2);

        // Ensure sufficient balance
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Insufficient funds.", ErrorCode.INSUFFICIENT_BALANCE, 400);
        }

        // Create transfer record (NOW amount is not null before save)
        FundTransfer fundTransfer = new FundTransfer();
        fundTransfer.setTransactionReference(TransactionRefGenerator.generateRandomNumber(8));
        fundTransfer.setFromAccountId(senderAccount.getAccId());
        fundTransfer.setToAccountId(receiverAccount.getAccId());
        fundTransfer.setPurpose(req.getPurpose());
        fundTransfer.setTransferType(TransferType.INTERNAL);
        fundTransfer.setChannel(FundTransferChannel.IMPS);

        fundTransfer.setCurrency(senderAccount.getCurrency());
        fundTransfer.setAmount(amount);
        fundTransfer.setStatus(TransferStatus.PROCESSING);

        fundTransfer = fundTransferRepository.save(fundTransfer);

        try {
            // Update balances
            BigDecimal senderNewBalance = senderAccount.getBalance().subtract(amount);
            BigDecimal receiverNewBalance = receiverAccount.getBalance().add(amount);

            senderAccount.setBalance(senderNewBalance);
            receiverAccount.setBalance(receiverNewBalance);

            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);

            // Create ledger entries (Debit sender, Credit receiver)
            LedgerEntry senderDebitEntry = ledgerEntryFactory.debit(
                    senderAccount,
                    amount,
                    senderNewBalance,
                    LedgerEntryType.DEBIT,
                    "Transfer to " + receiverAccount.getAccNumber(),
                    Glcode.CASH
            );

            LedgerEntry receiverCreditEntry = ledgerEntryFactory.credit(
                    receiverAccount,
                    amount,
                    receiverNewBalance,
                    LedgerEntryType.CREDIT,
                    "Received from " + senderAccount.getAccNumber(),
                    Glcode.CASH
            );

            ledgerEntryRepository.save(senderDebitEntry);
            ledgerEntryRepository.save(receiverCreditEntry);

            // Mark SUCCESS
            fundTransfer.setStatus(TransferStatus.SUCCESS);
            fundTransfer.setCompletedAt(OffsetDateTime.now(ZoneOffset.UTC));
            fundTransferRepository.save(fundTransfer);

            return fundTransferMapper.toDto(fundTransfer);

        } catch (Exception ex) {
            markFailed(fundTransfer.getId(), ex.getMessage());
            throw ex;
        }
    }

    private BigDecimal resolveAmount(FundTransferRequest req, int scale) {
        // prepare to handle KHR and USD scale in future
        return req.getAmount().setScale(scale, RoundingMode.HALF_UP);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(Long transferId, String reason) {

        FundTransfer fundTransfer = fundTransferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("FundTransfer", transferId));

        fundTransfer.setStatus(TransferStatus.FAILED);
        fundTransfer.setFailedReason(reason);
        fundTransferRepository.save(fundTransfer);
    }


    @Override
    public List<FundTransferDTO> getAllFundTransfer() {
        return fundTransferMapper.toDto(fundTransferRepository.findAll());
    }

    @Override
    @Transactional
    public FundTransferDTO reverseFundTransfer(@NonNull ReverseFundTransferReq req) {

        // Fetch original transfer (LOCK)
        FundTransfer original = fundTransferRepository.findByIdForUpdate(req.getOriginalTransferId())
                .orElseThrow(() -> new ResourceNotFoundException("FundTransfer", req.getOriginalTransferId()));

        if (original.getStatus() == TransferStatus.REVERSED || original.getReversed()) {
            throw new BusinessException("This transfer is already reversed.",
                    ErrorCode.INVALID_OPERATION, 400);
        }


        // Only SUCCESS transfers can be reversed
        if (original.getStatus() != TransferStatus.SUCCESS) {
            throw new BusinessException("Only SUCCESS transfers can be reversed.",
                    ErrorCode.INVALID_OPERATION, 400);
        }

        // Load accounts (LOCK balances!)
        Account senderAccount = accountRepository.findByIdForUpdate(original.getFromAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", original.getFromAccountId()));

        Account receiverAccount = accountRepository.findByIdForUpdate(original.getToAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", original.getToAccountId()));

        BigDecimal amount = original.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Original transfer amount is invalid.",
                    ErrorCode.INVALID_OPERATION, 400);
        }

        // currency must match
        if (senderAccount.getCurrency() != receiverAccount.getCurrency()
                || original.getCurrency() != senderAccount.getCurrency()) {
            throw new BusinessException("Currency mismatch; cannot reverse transfer.",
                    ErrorCode.INVALID_OPERATION, 400);
        }

        // Receiver must have enough balance to reverse
        if (receiverAccount.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Receiver has insufficient balance to reverse this transfer.",
                    ErrorCode.INSUFFICIENT_BALANCE, 400);
        }

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        // Capture BEFORE balances (important!)
        BigDecimal senderBefore = senderAccount.getBalance();
        BigDecimal receiverBefore = receiverAccount.getBalance();

        // Compute AFTER balances
        BigDecimal receiverAfter = receiverBefore.subtract(amount);
        BigDecimal senderAfter = senderBefore.add(amount);

        // Apply balance reversal
        receiverAccount.setBalance(receiverAfter);
        senderAccount.setBalance(senderAfter);

        accountRepository.save(receiverAccount);
        accountRepository.save(senderAccount);

        // Create reversal transfer (swap from/to)
        FundTransfer reversal = new FundTransfer();
        reversal.setTransactionReference(TransactionRefGenerator.generateRandomNumber(24));
        reversal.setFromAccountId(receiverAccount.getAccId());
        reversal.setToAccountId(senderAccount.getAccId());
        reversal.setAmount(amount);
        reversal.setCurrency(original.getCurrency());

        String reversalPurpose = "REVERSAL of " + original.getTransactionReference()
                + (req.getReason() == null || req.getReason().isBlank() ? "" : " - " + req.getReason());
        reversal.setPurpose(reversalPurpose);

        reversal.setTransferType(original.getTransferType());
        reversal.setChannel(original.getChannel());
        reversal.setStatus(TransferStatus.SUCCESS);
        reversal.setCompletedAt(now);

        reversal = fundTransferRepository.save(reversal);

        // Ledger entries (use AFTER balances like your create transfer)
        LedgerEntry receiverReversalDebit = ledgerEntryFactory.debit(
                receiverAccount,
                amount,
                receiverAfter,
                LedgerEntryType.DEBIT,
                "Reversal to " + senderAccount.getAccNumber(),
                Glcode.CASH
        );

        LedgerEntry senderReversalCredit = ledgerEntryFactory.credit(
                senderAccount,
                amount,
                senderAfter,
                LedgerEntryType.CREDIT,
                "Reversal from " + receiverAccount.getAccNumber(),
                Glcode.CASH
        );

        ledgerEntryRepository.save(receiverReversalDebit);
        ledgerEntryRepository.save(senderReversalCredit);

        // Mark original as reversed
        original.setStatus(TransferStatus.REVERSED);
        original.setRevereAt(now);
        fundTransferRepository.save(original);

        return fundTransferMapper.toDto(reversal);
    }

}
