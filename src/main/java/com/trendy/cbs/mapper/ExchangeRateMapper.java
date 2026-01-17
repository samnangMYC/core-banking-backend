package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.ExchangeRate;
import com.trendy.cbs.payload.dto.ExchangeRateDTO;
import com.trendy.cbs.payload.request.ExchangeRateRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {

    ExchangeRate toEntity(ExchangeRateRequest request);

    ExchangeRateDTO toDTO(ExchangeRate entity);

    List<ExchangeRateDTO> toListDTO(List<ExchangeRate> entities);


}
