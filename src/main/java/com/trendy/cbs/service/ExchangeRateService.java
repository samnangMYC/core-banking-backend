package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.ExchangeRateDTO;
import com.trendy.cbs.payload.request.ExchangeRateRequest;

import java.util.List;

public interface ExchangeRateService {
    ExchangeRateDTO createNewRate(ExchangeRateRequest req);

    List<ExchangeRateDTO> getAllRate();
}
