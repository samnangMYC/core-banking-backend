package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.ExchangeRateDTO;
import com.trendy.cbs.payload.request.ExchangeRateRequest;
import com.trendy.cbs.service.ExchangeRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/currency/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService rateService;

    @PostMapping
    public ResponseEntity<ExchangeRateDTO> createExchangeRate(@Valid @RequestBody ExchangeRateRequest req) {
        log.info("Creating a new rate for currency: {}", req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rateService.createNewRate(req));
    }
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','MANAGER','OPERATIONS')")
    @GetMapping
    public ResponseEntity<List<ExchangeRateDTO>> getAllExchangeRate() {
        log.info("Retrieving all rate for currency....");
        return ResponseEntity.ok(rateService.getAllRate());

    }
}
