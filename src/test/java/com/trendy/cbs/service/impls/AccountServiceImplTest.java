package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.*;
import com.trendy.cbs.enums.*;
import com.trendy.cbs.exception.BusinessException;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.mapper.AccountMapper;
import com.trendy.cbs.payload.dto.AccountDTO;
import com.trendy.cbs.payload.request.CreateSelfAccountReq;
import com.trendy.cbs.payload.request.DepositReq;
import com.trendy.cbs.repos.*;
import com.trendy.cbs.service.UserService;
import com.trendy.cbs.service.factory.LedgerEntryFactory;
import com.trendy.cbs.service.validation.AccountValidationService;
import com.trendy.cbs.service.validation.CustomerValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AccountTypeRepository accountTypeRepository;
    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private CustomerValidationService customerValidationService;
    @Mock
    private LedgerEntryRepository ledgerEntryRepository;
    @Mock
    private AccountValidationService accountValidationService;
    @Mock
    private LedgerEntryFactory ledgerEntryFactory;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private Jwt jwt;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User user;
    private Customer customer;
    private Account account;
    private AccountDTO accountDTO;

    // Customer Module Unit Testing
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        customer = new Customer();
        customer.setId(10L);
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setPhoneNumber("0123456789");
        customer.setIdentityDoc(null);


        Currency currency = new Currency();
        currency.setCurId(1L);
        currency.setCode("USD");


        AccountType accountType = new AccountType();
        accountType.setId(1L);


        account = new Account();
        account.setAccId(100L);
        account.setAccNumber("10000001");
        account.setBalance(new BigDecimal("500"));
        account.setStatus(AccountStatus.ACTIVE);
        account.setCurrency(currency);
        account.setCustomer(customer);


        accountDTO = new AccountDTO();
        accountDTO.setAccId(100L);
        accountDTO.setBalance(new BigDecimal("1500"));

        lenient().when(userService.loadUserByJwt(jwt)).thenReturn(user);
        lenient().when(customerRepository.findByUser_Id(user.getId())).thenReturn(Optional.of(customer));
    }

    // Create Account Testing
    @Test
    void createSelfAccount_firstAccount_createsDefaultSavingsUsdAccount() {
        when(accountRepository.countByCustomerId(customer.getId())).thenReturn(0);
        when(accountRepository.existsByCustomer(customer)).thenReturn(false);

        AccountType savingsType = new AccountType();
        savingsType.setId(1L);
        savingsType.setPurposeType(PurposeType.SAVINGS);
        when(accountTypeRepository.findByPurposeType(PurposeType.SAVINGS)).thenReturn(Optional.of(savingsType));

        Currency usd = new Currency();
        usd.setCurId(1L);
        usd.setCode("USD");
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(usd));

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(AccountDTO.builder().accNumber(customer.getPhoneNumber()).build());

        AccountDTO result = accountService.createSelfAccount(jwt, new CreateSelfAccountReq(2L, 2L));

        assertThat(result.getAccNumber()).isEqualTo(customer.getPhoneNumber());
        verify(accountTypeRepository).findByPurposeType(PurposeType.SAVINGS);
        verify(currencyRepository).findByCode("USD");
        verify(accountTypeRepository, never()).findById(any());
        verify(currencyRepository, never()).findById(any());
    }

    @Test
    void createSelfAccount_subsequentAccount_usesRequestedTypeAndCurrency() {
        CreateSelfAccountReq request = new CreateSelfAccountReq(5L, 7L);

        when(accountRepository.countByCustomerId(customer.getId())).thenReturn(1);
        when(accountRepository.existsByCustomer(customer)).thenReturn(true);

        AccountType requestedType = new AccountType();
        requestedType.setId(5L);
        when(accountTypeRepository.findById(5L)).thenReturn(Optional.of(requestedType));

        Currency requestedCurrency = new Currency();
        requestedCurrency.setCurId(7L);
        when(currencyRepository.findById(7L)).thenReturn(Optional.of(requestedCurrency));

        when(accountRepository.existsByAccNumber(any())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(AccountDTO.builder().build());

        accountService.createSelfAccount(jwt, request);

        verify(accountTypeRepository).findById(5L);
        verify(currencyRepository).findById(7L);
        verify(accountTypeRepository, never()).findByPurposeType(any());
    }

    @Test
    void createSelfAccount_customerNotFound_throwsBusinessException() {
        when(customerRepository.findByUser_Id(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.createSelfAccount(jwt, new CreateSelfAccountReq(1L, 1L)))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode()).isEqualTo(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Test
    void createSelfAccount_accountLimitReached_throwsBusinessException() {
        when(accountRepository.countByCustomerId(customer.getId())).thenReturn(15);

        assertThatThrownBy(() -> accountService.createSelfAccount(jwt, new CreateSelfAccountReq(1L, 1L)))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode()).isEqualTo(ErrorCode.ACCOUNT_LIMIT_REACHED));

        verify(customerValidationService, never()).validateCustomer(any(), any());
    }

    @Test
    void createSelfAccount_customerNotActive_propagatesValidationException() {
        when(accountRepository.countByCustomerId(customer.getId())).thenReturn(0);
        doThrow(new BusinessException("Customer is not active", ErrorCode.CUSTOMER_NOT_ACTIVE, 409))
                .when(customerValidationService).validateCustomer(customer, null);

        assertThatThrownBy(() -> accountService.createSelfAccount(jwt, new CreateSelfAccountReq(1L, 1L)))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode()).isEqualTo(ErrorCode.CUSTOMER_NOT_ACTIVE));

        verify(accountRepository, never()).existsByCustomer(any());
    }

    @Test
    void createSelfAccount_defaultAccountTypeMissing_throwsResourceNotFoundException() {
        when(accountRepository.countByCustomerId(customer.getId())).thenReturn(0);
        when(accountRepository.existsByCustomer(customer)).thenReturn(false);
        when(accountTypeRepository.findByPurposeType(PurposeType.SAVINGS)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.createSelfAccount(jwt, new CreateSelfAccountReq(1L, 1L)))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(accountRepository, never()).save(any());
    }

    @Test
    void createSelfAccount_defaultCurrencyMissing_throwsResourceNotFoundException() {
        when(accountRepository.countByCustomerId(customer.getId())).thenReturn(0);
        when(accountRepository.existsByCustomer(customer)).thenReturn(false);

        AccountType savingsType = new AccountType();
        savingsType.setPurposeType(PurposeType.SAVINGS);
        when(accountTypeRepository.findByPurposeType(PurposeType.SAVINGS)).thenReturn(Optional.of(savingsType));
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.createSelfAccount(jwt, new CreateSelfAccountReq(1L, 1L)))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(accountRepository, never()).save(any());
    }

    @Test
    void createSelfAccount_requestedAccountTypeMissing_throwsResourceNotFoundException() {
        CreateSelfAccountReq request = new CreateSelfAccountReq(99L, 7L);

        when(accountRepository.countByCustomerId(customer.getId())).thenReturn(1);
        when(accountRepository.existsByCustomer(customer)).thenReturn(true);
        when(accountTypeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.createSelfAccount(jwt, request))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(accountRepository, never()).save(any());
    }


    // Get Account By Id Testing
    @Test
    void getAccountById_accountExists_returnsMappedAccountDTO() {
        Account account = new Account();
        account.setAccId(1L);
        account.setAccNumber("0123456789");

        AccountDTO expectedDto = AccountDTO.builder().accNumber(account.getAccNumber()).build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.toDTO(account)).thenReturn(expectedDto);

        AccountDTO result = accountService.getAccountById(1L);

        assertThat(result.getAccNumber()).isEqualTo(account.getAccNumber());
        verify(accountRepository).findById(1L);
        verify(accountMapper).toDTO(account);
    }

    @Test
    void getAccountById_accountNotFound_throwsResourceNotFoundException() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(accountMapper, never()).toDTO(any(Account.class));
    }

    // Staff Account Module Unit Testing

    // -------- DEPOSIT ----------- //
    @Test
    void deposit_shouldIncreaseBalance_whenValidRequest() {
        // Arrange
        DepositReq request = new DepositReq();
        request.setAmount(new BigDecimal("1000"));

        when(accountRepository.findById(100L))
                .thenReturn(Optional.of(account));

        // Validation passes
        doNothing().when(accountValidationService)
                .validateAccountStatus(account);

        // Mock ledger creation
        LedgerEntry ledgerEntry = new LedgerEntry();

        when(ledgerEntryFactory.credit(
                eq(account),
                eq(new BigDecimal("1000")),
                eq(new BigDecimal("1500")),
                eq(LedgerEntryType.CREDIT),
                eq("Cash Deposit"),
                eq(Glcode.CASH)
        )).thenReturn(ledgerEntry);

        when(ledgerEntryRepository.save(ledgerEntry))
                .thenReturn(ledgerEntry);

        // Repository save must return the saved account
        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);

        // Mapper
        when(accountMapper.toDTO(account))
                .thenReturn(accountDTO);

        // Act
        AccountDTO result = accountService.deposit(100L, request);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("1500"), result.getBalance());

        verify(accountValidationService).validateAccountStatus(account);
        verify(ledgerEntryRepository).save(ledgerEntry);
        verify(accountRepository).save(account);
        verify(accountMapper).toDTO(account);
    }

    // -------- WITHDRAW ----------- //
    @Test
    void withdraw_shouldDecreaseBalance_whenValidRequest() {
        // Arrange
        DepositReq request = new DepositReq();
        request.setAmount(new BigDecimal("200"));

        LedgerEntry ledgerEntry = new LedgerEntry();

        when(accountRepository.findById(100L))
                .thenReturn(Optional.of(account));

        doNothing().when(accountValidationService)
                .validateAccountStatus(account);

        when(ledgerEntryFactory.debit(
                eq(account),
                eq(new BigDecimal("200")),
                eq(new BigDecimal("300")),
                eq(LedgerEntryType.DEBIT),
                eq("Withdrawal"),
                eq(Glcode.CASH)
        )).thenReturn(ledgerEntry);

        when(ledgerEntryRepository.save(ledgerEntry))
                .thenReturn(ledgerEntry);

        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);

        accountDTO.setBalance(new BigDecimal("300"));

        when(accountMapper.toDTO(account))
                .thenReturn(accountDTO);

        // Act
        AccountDTO result = accountService.withdraw(100L, request);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("300"), result.getBalance());

        verify(accountValidationService).validateAccountStatus(account);

        verify(ledgerEntryFactory).debit(
                eq(account),
                eq(new BigDecimal("200")),
                eq(new BigDecimal("300")),
                eq(LedgerEntryType.DEBIT),
                eq("Withdrawal"),
                eq(Glcode.CASH)
        );

        verify(ledgerEntryRepository).save(ledgerEntry);
        verify(accountRepository).save(account);
        verify(accountMapper).toDTO(account);
    }

    @Test
    void withdraw_shouldThrowException_whenAccountNotFound() {

        DepositReq request = new DepositReq();
        request.setAmount(new BigDecimal("200"));

        when(accountRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> accountService.withdraw(100L, request)
        );

        verify(accountRepository, never()).save(any());
        verifyNoInteractions(ledgerEntryRepository);
    }

    @Test
    void withdraw_shouldThrowException_whenAccountInactive() {

        DepositReq request = new DepositReq();
        request.setAmount(new BigDecimal("200"));

        when(accountRepository.findById(100L))
                .thenReturn(Optional.of(account));

        doThrow(new IllegalStateException("Account is inactive"))
                .when(accountValidationService)
                .validateAccountStatus(account);

        assertThrows(
                IllegalStateException.class,
                () -> accountService.withdraw(100L, request)
        );

        verify(accountRepository, never()).save(any());
        verifyNoInteractions(ledgerEntryRepository);
    }

    @Test
    void withdraw_shouldThrowBusinessException_whenInsufficientBalance() {

        DepositReq request = new DepositReq();
        request.setAmount(new BigDecimal("1000"));

        when(accountRepository.findById(100L))
                .thenReturn(Optional.of(account));

        doNothing().when(accountValidationService)
                .validateAccountStatus(account);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> accountService.withdraw(100L, request)
        );

        assertEquals(
                ErrorCode.INSUFFICIENT_BALANCE,
                exception.getErrorCode()
        );

        assertEquals(
                "Insufficient balance",
                exception.getMessage()
        );

        verify(accountRepository, never()).save(any());
        verifyNoInteractions(ledgerEntryRepository);
    }


}