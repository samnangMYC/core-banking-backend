package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Account;
import com.trendy.cbs.entity.FundTransfer;
import com.trendy.cbs.entity.LedgerEntry;
import com.trendy.cbs.enums.*;
import com.trendy.cbs.exception.BusinessException;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.helper.TransactionRefGenerator;
import com.trendy.cbs.mapper.FundTransferMapper;
import com.trendy.cbs.payload.dto.FundTransferDTO;
import com.trendy.cbs.payload.request.FundTransferRequest;
import com.trendy.cbs.repos.AccountRepository;
import com.trendy.cbs.repos.FundTransferRepository;
import com.trendy.cbs.repos.LedgerEntryRepository;
import com.trendy.cbs.service.FundTransferService;
import com.trendy.cbs.service.factory.LedgerEntryFactory;
import com.trendy.cbs.service.validation.AccountValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
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

        // Fetch sender account
        Account senderAccount = accountRepository.findById(req.getFromAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", req.getFromAccountId()));

        // Fetch receiver account
        Account receiverAccount = accountRepository.findById(req.getToAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", req.getToAccountId()));

        // Prevent self-transfer
        if (senderAccount.getAccId().equals(receiverAccount.getAccId())) {
            throw new BusinessException("Sender and receiver cannot be the same account.",
                    ErrorCode.INVALID_OPERATION, 400);
        }

        // Validate accounts status
        accountValidationService.validateAccountStatus(senderAccount);
        accountValidationService.validateAccountStatus(receiverAccount);

        // Validate transfer amount
        BigDecimal transferAmount = req.getAmount();

        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Transfer amount must be greater than zero.",
                    ErrorCode.INVALID_AMOUNT, 400);
        }

        // Check sufficient available balance
        BigDecimal senderAvailableBalance = senderAccount.getAvailableBalance();
        if (senderAvailableBalance.compareTo(transferAmount) < 0) {
            throw new BusinessException("Insufficient balance to complete the transaction.",
                    ErrorCode.INSUFFICIENT_BALANCE, 400);
        }

        // Create fund transfer record
        FundTransfer fundTransfer = new FundTransfer();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        fundTransfer.setTransactionReference(TransactionRefGenerator.generateRandomNumber(24));
        fundTransfer.setFromAccountId(senderAccount.getAccId());
        fundTransfer.setToAccountId(receiverAccount.getAccId());
        fundTransfer.setAmount(transferAmount);
        fundTransfer.setCurrency(senderAccount.getCurrency());
        fundTransfer.setPurpose(req.getPurpose());
        fundTransfer.setTransferType(TransferType.INTERNAL);
        fundTransfer.setChannel(FundTransferChannel.IMPS);
        fundTransfer.setStatus(TransferStatus.SUCCESS);
        fundTransfer.setCompletedAt(now);

        // Save fund transfer
        fundTransferRepository.save(fundTransfer);

        // Update balances
        senderAccount.setBalance(senderAccount.getBalance().subtract(transferAmount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(transferAmount));

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // Create ledger entries
        LedgerEntry senderLedgerEntry = ledgerEntryFactory.debit(
                senderAccount,
                transferAmount,
                senderAccount.getBalance(),
                LedgerEntryType.DEBIT,
                "Transfer to " + receiverAccount.getAccNumber(),
                Glcode.CASH
        );

        LedgerEntry receiverLedgerEntry = ledgerEntryFactory.credit(
                receiverAccount,
                transferAmount,
                receiverAccount.getBalance(),
                LedgerEntryType.CREDIT,
                "Received from " + senderAccount.getAccNumber(),
                Glcode.CASH
        );

        ledgerEntryRepository.save(senderLedgerEntry);
        ledgerEntryRepository.save(receiverLedgerEntry);

        // Return fund transfer DTO
        return fundTransferMapper.toDto(fundTransfer);
    }

    @Override
    public List<FundTransferDTO> getAllFundTransfer() {
        return fundTransferMapper.toDto(fundTransferRepository.findAll());
    }
}
