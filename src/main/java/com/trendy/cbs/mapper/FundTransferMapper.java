package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.FundTransfer;
import com.trendy.cbs.payload.dto.FundTransferDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FundTransferMapper {
    FundTransferDTO toDto(FundTransfer entity);

    List<FundTransferDTO> toDto(List<FundTransfer> entity);
}
