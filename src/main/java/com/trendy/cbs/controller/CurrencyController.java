package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.CurrencyDTO;
import com.trendy.cbs.payload.request.CurrencyRequest;
import com.trendy.cbs.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    /**
     * Creates a new currency based on the provided request.
     *
     * @param request the request body containing the details of the currency to create
     * @return a ResponseEntity containing the created CurrencyDTO with HTTP status OK
     */
    @PostMapping
    public ResponseEntity<@NonNull CurrencyDTO> createCurrency(@Valid @RequestBody CurrencyRequest request){
        log.info("Received request to create a currency{}", request);
        return ResponseEntity.ok(currencyService.createCurrency(request));

    }

    /**
     * Retrieves all available currencies.
     *
     * @return a ResponseEntity containing a list of all CurrencyDTO with HTTP status OK
     */
    @GetMapping
    public ResponseEntity<@NonNull List<CurrencyDTO>> getAllCurrency(){
        log.info("Received request to get all currencies");
        return ResponseEntity.ok(currencyService.getAllCurrency());
    }

    /**
     * Retrieves a specific currency by its ID.
     *
     * @param id the ID of the currency to retrieve
     * @return a ResponseEntity containing the CurrencyDTO for the specified ID with HTTP status OK
     */
    @GetMapping("{id}")
    public ResponseEntity<@NonNull CurrencyDTO> getCurrencyById(@PathVariable Long id){
        log.info("Received request to get currency by id: {}", id);
        return ResponseEntity.ok(currencyService.getCurrencyById(id));
    }

    /**
     * Updates an existing currency by its ID with the provided request details.
     *
     * @param id the ID of the currency to update
     * @param request the request body containing the updated details of the currency
     * @return a ResponseEntity containing the updated CurrencyDTO with HTTP status OK
     */
    @PutMapping("{id}")
    public ResponseEntity<@NonNull CurrencyDTO> updateCurrency(@PathVariable Long id, @Valid @RequestBody CurrencyRequest request){
        log.info("Received request to update a currency by id: {}", id);
        return ResponseEntity.ok(currencyService.updateCurrencyById(id,request));
    }

    /**
     * Deletes a currency by its ID.
     *
     * @param id the ID of the currency to delete
     * @return a ResponseEntity containing a confirmation message with HTTP status OK
     */
    @DeleteMapping("{id}")
    public ResponseEntity<@NonNull String> deleteCurrency(@PathVariable Long id){
        log.info("Received request to delete a currency by id: {}", id);
        return ResponseEntity.ok(currencyService.deleteCurrencyById(id));
    }
}
