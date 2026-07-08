package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.AccountDTO;
import com.trendy.cbs.payload.dto.BalanceDTO;
import com.trendy.cbs.payload.request.CreateAccountReq;
import com.trendy.cbs.payload.request.CreateSelfAccountReq;
import com.trendy.cbs.payload.request.AccountStatusReq;
import com.trendy.cbs.payload.request.DepositReq;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface AccountService {

    AccountDTO createSelfAccount(Jwt jwt, CreateSelfAccountReq request);

    List<AccountDTO> getAllAccounts();

    AccountDTO getAccountById(Long id);

    AccountDTO updateAccountStatus(Long id, AccountStatusReq request);

    AccountDTO getAccountByAccountNumber(String accNumber);

    BalanceDTO getAccountBalance(Long id);

    AccountDTO deposit(Long id, DepositReq req);

    String deleteAccountById(String id);

    AccountDTO withdraw(Long id, @Valid DepositReq req);

    AccountDTO createAccountByStaff(@Valid CreateAccountReq req);

    AccountDTO getSelfAccountByJwt(Jwt jwt);
}
