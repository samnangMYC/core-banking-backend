package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.AccountType;
import com.trendy.cbs.payload.dto.AccountTypeDTO;
import com.trendy.cbs.payload.request.AccountTypeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountTypeMapper {

    AccountType toEntity(AccountTypeRequest accountTypeRequest);

    AccountTypeDTO toDTO(AccountType accountType);

    List<AccountTypeDTO> toListDTO(List<AccountType> accountTypes);

    void updateEntityFromRequest(AccountTypeRequest request,@MappingTarget AccountType accountType);
}
