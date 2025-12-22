package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.LedgerEntry;
import com.trendy.cbs.payload.dto.LedgerEntryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LedgerEntryMapper {

    @Mapping(source = "account.accId",target = "accountId")
    LedgerEntryDTO toDto(LedgerEntry ledgerEntry);

    LedgerEntry toEntity(LedgerEntryDTO ledgerEntryDTO);

    List<LedgerEntryDTO> toListDto(List<LedgerEntry> entry);
}
