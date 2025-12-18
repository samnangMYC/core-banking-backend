package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.AccountDTO;
import com.trendy.cbs.payload.request.AccountRequest;
import com.trendy.cbs.payload.request.AccountStatusReq;
import com.trendy.cbs.payload.request.DepositReq;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    AccountDTO createNewAccount(Long customerId,AccountRequest request);

    List<AccountDTO> getAllAccounts();

    AccountDTO getAccountById(Long id);

    AccountDTO updateAccountStatus(Long id, AccountStatusReq request);

    AccountDTO getAccountByAccountNumber(String accNumber);

    BigDecimal getAccountBalance(Long id);

    AccountDTO updateBalance(Long id, DepositReq req);

    String deleteAccountById(String id);
}
