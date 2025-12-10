package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Currency;
import com.trendy.cbs.exception.DuplicationResource;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.mapper.CurrencyMapper;
import com.trendy.cbs.payload.dto.CurrencyDTO;
import com.trendy.cbs.payload.request.CurrencyRequest;
import com.trendy.cbs.repos.AccountTypeRepository;
import com.trendy.cbs.repos.CurrencyRepository;
import com.trendy.cbs.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of the CurrencyService interface providing CRUD operations for currencies.
 */
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    /**
     * Creates a new currency based on the provided request.
     *
     * @param request the CurrencyRequest containing details for the new currency
     * @return the created CurrencyDTO
     * @throws DuplicationResource if a currency with the same code already exists
     */
    @Override
    public CurrencyDTO createCurrency(CurrencyRequest request) {

        Optional<Currency> currency =  currencyRepository.findByCode(request.getCode());
        if(currency.isPresent()) {
            throw new DuplicationResource("Currency code already exists");
        }

        return currencyMapper.toDto(currencyRepository.save(currencyMapper.toEntity(request)));
    }

    /**
     * Retrieves a list of all currencies.
     *
     * @return a list of CurrencyDTOs representing all currencies
     */
    @Override
    public List<CurrencyDTO> getAllCurrency() {
        return  currencyMapper.toDtoList(currencyRepository.findAll());
    }

    /**
     * Retrieves a currency by its ID.
     *
     * @param id the ID of the currency to retrieve
     * @return the CurrencyDTO for the specified ID
     * @throws ResourceNotFoundException if no currency is found with the given ID
     */
    @Override
    public CurrencyDTO getCurrencyById(Long id) {
        return currencyRepository.findById(id)
                .map(currencyMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Currency",id));
    }

    /**
     * Updates an existing currency by its ID with the provided request details.
     *
     * @param id the ID of the currency to update
     * @param request the CurrencyRequest containing updated details
     * @return the updated CurrencyDTO
     * @throws ResourceNotFoundException if no currency is found with the given ID
     */
    @Override
    public CurrencyDTO updateCurrencyById(Long id, CurrencyRequest request) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency",id));

        currencyMapper.updateEntityFromRequest(request, currency);

        return currencyMapper.toDto(currencyRepository.save(currency));
    }

    /**
     * Deletes a currency by its ID.
     *
     * @param id the ID of the currency to delete
     * @return a confirmation message indicating successful deletion
     * @throws ResourceNotFoundException if no currency is found with the given ID
     */
    @Override
    public String deleteCurrencyById(Long id) {
        currencyRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Currency",id));

        currencyRepository.deleteById(id);

        return "Currency was deleted successfully!!";
    }


}

