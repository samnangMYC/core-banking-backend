package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.CurrencyDTO;
import com.trendy.cbs.payload.request.CurrencyRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface CurrencyService {
   CurrencyDTO createCurrency(CurrencyRequest request);

   List<CurrencyDTO> getAllCurrency();

   CurrencyDTO getCurrencyById(Long id);

   CurrencyDTO updateCurrencyById(Long id, @Valid CurrencyRequest request);

   String deleteCurrencyById(Long id);
}
