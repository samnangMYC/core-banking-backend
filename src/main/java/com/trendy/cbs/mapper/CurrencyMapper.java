package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.Currency;
import com.trendy.cbs.payload.dto.CurrencyDTO;
import com.trendy.cbs.payload.request.CurrencyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    Currency toEntity(CurrencyRequest request);

    CurrencyDTO toDto(Currency entity);

    List<CurrencyDTO> toDtoList(List<Currency> currencies);

    void updateEntityFromRequest(CurrencyRequest request, @MappingTarget Currency currency);
}
