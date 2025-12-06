package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.AccountTypeDTO;
import com.trendy.cbs.payload.request.AccountTypeRequest;

import java.util.List;

public interface AccountTypeService {
    AccountTypeDTO createAccountType(AccountTypeRequest request);

    List<AccountTypeDTO> getAllAccountTypes();

    AccountTypeDTO getAccountTypeById(Long id);

    AccountTypeDTO updateAccountType(Long id, AccountTypeRequest request);

    String deleteAccountTypeById(Long id);
}
