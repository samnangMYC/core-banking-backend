package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.*;
import com.trendy.cbs.enums.*;
import com.trendy.cbs.exception.BusinessException;
import com.trendy.cbs.exception.DuplicationResource;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.helper.AccountNumberGenerator;
import com.trendy.cbs.helper.TransactionReferenceGenerator;
import com.trendy.cbs.mapper.AccountMapper;
import com.trendy.cbs.mapper.AccountTypeMapper;
import com.trendy.cbs.mapper.CurrencyMapper;
import com.trendy.cbs.mapper.CustomerMapper;
import com.trendy.cbs.payload.dto.AccountDTO;
import com.trendy.cbs.payload.request.AccountRequest;
import com.trendy.cbs.payload.request.AccountStatusReq;
import com.trendy.cbs.payload.request.DepositReq;
import com.trendy.cbs.repos.*;
import com.trendy.cbs.service.AccountService;
import com.trendy.cbs.service.validation.CustomerValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.trendy.cbs.helper.AccountNumberGenerator.generateUniqueAccountNumber;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerRepository customerRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final CurrencyRepository currencyRepository;
    private final BranchRepository branchRepository;
    private final CustomerValidationService customerValidationService;
    private final LedgerEntryRepository ledgerEntryRepository;

    private static final int MAX_ACCOUNTS_PER_CUSTOMER = 15;
    private final TransactionReferenceGenerator transactionReferenceGenerator;


    @Override
    public AccountDTO createNewAccount(Long customerId,AccountRequest request) {

        // Fetch customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", request.getBranchId()));

        // Check account limits
        Integer existAccounts = accountRepository.countByCustomerCusId(customerId);
        if (existAccounts >= MAX_ACCOUNTS_PER_CUSTOMER) {
            throw new BusinessException(
                    "Customer has reached maximum allowed accounts",
                    ErrorCode.ACCOUNT_LIMIT_REACHED,
                    HttpStatus.BAD_REQUEST.value()
            );
        }

        // customer verification (purpose: make sure customer verify their kyc information and identity document)

        IdentityDoc identityDoc = customer.getIdentityDoc();

        customerValidationService.validateCustomer(customer, identityDoc);

        // ---------- Process 1: Automatic account creation ----------
        Boolean customerHasAccounts = accountRepository.existsByCustomer(customer);
       // System.out.println("customerHasAccounts = " + customerHasAccounts);

        Account account;
        if (!customerHasAccounts) {

            // No accounts yet → create default account (Saving, USD)
            AccountType defaultType = accountTypeRepository.findByCode("SAV001")
                    .orElseThrow(() -> new ResourceNotFoundException("AccountType", "SAV001"));

            Currency usdCurrency = currencyRepository.findByCode("USD")
                    .orElseThrow(() -> new ResourceNotFoundException("Currency", "USD"));

            account = new Account();
            account.setCustomer(customer);
            account.setAccountType(defaultType);
            account.setCurrency(usdCurrency);
            account.setBranch(branch);
            account.setStatus(AccountStatus.ACTIVE);
            account.setAccNumber(customer.getProfile().getPhoneNumber());
            account.setClosedAt(null);

            accountRepository.save(account);
        } else {
            // ---------- Process 2: Manual account creation ----------
            // Fetch requested account type, currency, and branch
            AccountType requestedType = accountTypeRepository.findById(request.getAccountTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("AccountType", request.getAccountTypeId()));

            Currency requestedCurrency = currencyRepository.findById(request.getCurrencyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Currency", request.getCurrencyId()));

            account = new Account();
            account.setCustomer(customer);
            account.setAccountType(requestedType);
            account.setCurrency(requestedCurrency);
            account.setBranch(branch);
            account.setStatus(AccountStatus.ACTIVE);
            account.setAccNumber(generateUniqueAccountNumber(accountRepository));
            account.setClosedAt(null);

            accountRepository.save(account);
        }

        return accountMapper.toDTO(accountRepository.save(account));

    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountMapper.toDTO(accountRepository.findAll());
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Accounts",id));
    }

    @Override
    public AccountDTO getAccountByAccountNumber(String accNumber) {
        return accountRepository.findByAccNumber(accNumber)
                .map(accountMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Account",accNumber));
    }

    @Override
    public BigDecimal getAccountBalance(Long id) {
        return accountRepository.findById(id)
                .map(Account::getBalance)
                .orElseThrow(() -> new ResourceNotFoundException("Account",id));
    }

    @Override
    public AccountDTO deposit(Long id, DepositReq req) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account",id));

        if (!AccountStatus.ACTIVE.equals(account.getStatus())) {
            throw new BusinessException(
                    "Cannot update balance: Account is not active",
                    ErrorCode.CUSTOMER_NOT_ACTIVE,
                    HttpStatus.BAD_REQUEST.value()
            );
        }
        BigDecimal depositBalance = calculateDepositBalance(account,req);

        // Create Ledger Entry (Using Provided Entity - Credit for Deposit)
        LedgerEntry ledgerEntry = LedgerEntry.builder()
                .account(account)
                .transactionReference(transactionReferenceGenerator.generate())
                .creditAmount(depositBalance)
                .runningBalance(account.getBalance())
                .type(LedgerEntryType.CREDIT)
                .description("")
                .postedAt(LocalDateTime.now())
                .glCode(Glcode.CASH)
                .build();

        ledgerEntryRepository.save(ledgerEntry);

        account.setBalance(depositBalance);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDTO(savedAccount);
    }

    private BigDecimal calculateDepositBalance(Account account, DepositReq req) {
        BigDecimal amount = req.getAmount();

        return account.getBalance().add(amount);
    }

    @Override
    public AccountDTO withdraw(Long id, DepositReq req) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account",id));

        if (!AccountStatus.ACTIVE.equals(account.getStatus())) {
            throw new BusinessException(
                    "Cannot update balance: Account is not active",
                    ErrorCode.CUSTOMER_NOT_ACTIVE,
                    HttpStatus.BAD_REQUEST.value()
            );
        }
        BigDecimal depositBalance = calculateWithdrawBalance(account,req);

        // Create Ledger Entry (Using Provided Entity - Credit for Deposit)
        LedgerEntry ledgerEntry = LedgerEntry.builder()
                .account(account)
                .transactionReference(transactionReferenceGenerator.generate())
                .creditAmount(depositBalance)
                .runningBalance(account.getBalance())
                .type(LedgerEntryType.CREDIT)
                .description("")
                .postedAt(LocalDateTime.now())
                .glCode(Glcode.CASH)
                .build();

        ledgerEntryRepository.save(ledgerEntry);

        account.setBalance(depositBalance);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDTO(savedAccount);
    }

    private BigDecimal calculateWithdrawBalance(Account account, DepositReq req) {
        BigDecimal balance = account.getBalance();
        BigDecimal amount = req.getAmount();

        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(
                    "Not enough balance to withdraw",
                    ErrorCode.INVALID_AMOUNT,
                    HttpStatus.BAD_REQUEST.value()
            );
        }


        return account.getBalance().subtract(amount);
    }

    @Override
    public String deleteAccountById(String id) {
        Account account = accountRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("Account",id));

        accountRepository.delete(account);

        return "Delete account by id " + id;
    }

    // ACTIVE, DORMANT, CLOSED, FROZEN
    @Override
    public AccountDTO updateAccountStatus(Long id, AccountStatusReq request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account",id));
        switch (request.getStatus()) {
            case ACTIVE:
                account.setStatus(AccountStatus.ACTIVE);
                break;

            case DORMANT:
                account.setStatus(AccountStatus.DORMANT);
                break;

            case CLOSED:
                account.setStatus(AccountStatus.CLOSED);
                account.setClosedAt(LocalDateTime.now());
                break;

            default:
                throw new IllegalArgumentException("Unsupported status: " + request.getStatus());
        }

        return accountMapper.toDTO(accountRepository.save(account));
    }


}
