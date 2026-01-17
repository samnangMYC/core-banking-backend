package com.trendy.cbs.service.validation;

import com.trendy.cbs.entity.Account;
import com.trendy.cbs.enums.AccountStatus;
import com.trendy.cbs.enums.ErrorCode;
import com.trendy.cbs.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AccountValidationService {

    public void validateAccountStatus(Account account) {
        AccountStatus accountStatus = account.getStatus();
        if (!AccountStatus.ACTIVE.equals(accountStatus)) {
            throw new BusinessException(
                    "Account is suspended or blocked.",
                    ErrorCode.CUSTOMER_NOT_ACTIVE,
                    HttpStatus.BAD_REQUEST.value()
            );
        }
    }
}
