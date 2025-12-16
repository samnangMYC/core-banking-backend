package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.Account;
import com.trendy.cbs.payload.dto.AccountDTO;
import com.trendy.cbs.payload.request.AccountRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toEntity(AccountRequest accountRequest);

    AccountDTO toDTO(Account account);

    List<AccountDTO> toDTO(List<Account> accounts);
}
