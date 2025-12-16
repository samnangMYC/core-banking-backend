package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.AccountDTO;
import com.trendy.cbs.payload.request.AccountRequest;
import com.trendy.cbs.payload.request.AccountStatusReq;

import java.util.List;

public interface AccountService {
    AccountDTO createNewAccount(Long customerId,AccountRequest request);

    List<AccountDTO> getAllAccounts();

    AccountDTO getAccountById(Long id);

    AccountDTO updateAccountStatus(Long id, AccountStatusReq request);
}
