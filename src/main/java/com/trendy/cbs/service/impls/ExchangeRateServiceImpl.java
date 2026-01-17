package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Currency;
import com.trendy.cbs.entity.ExchangeRate;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.mapper.ExchangeRateMapper;
import com.trendy.cbs.payload.dto.ExchangeRateDTO;
import com.trendy.cbs.payload.request.ExchangeRateRequest;
import com.trendy.cbs.repos.CurrencyRepository;
import com.trendy.cbs.repos.ExchangeRateRepository;
import com.trendy.cbs.service.ExchangeRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateMapper exchangeRateMapper;

    @Override
    public ExchangeRateDTO createNewRate(ExchangeRateRequest req) {

        Long fromCurId = req.getFromCurrencyId();
        Long toCurId = req.getToCurrencyId();

        Currency fromCur = currencyRepository.findById(fromCurId)
                .orElseThrow(() -> new ResourceNotFoundException("From Currency",fromCurId));

        Currency toCur = currencyRepository.findById(toCurId)
                .orElseThrow(() -> new ResourceNotFoundException("To Currency",toCurId));


        ExchangeRate entity = exchangeRateMapper.toEntity(req);
        entity.setFromCurrency(fromCur);
        entity.setToCurrency(toCur);

        return exchangeRateMapper.toDTO(exchangeRateRepository.save(entity));
    }

    @Override
    public List<ExchangeRateDTO> getAllRate() {
        return exchangeRateMapper.toListDTO(exchangeRateRepository.findAll());
    }
}
