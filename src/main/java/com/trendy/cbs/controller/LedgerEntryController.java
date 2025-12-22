package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.LedgerEntryDTO;
import com.trendy.cbs.service.LedgerEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/ledger-entry")
@RequiredArgsConstructor
public class LedgerEntryController {

    private final LedgerEntryService ledgerEntryService;

    @GetMapping
    public ResponseEntity<List<LedgerEntryDTO>> getAllLedgerEntries() {
        log.debug("fetching all ledger entries.......");
        return ResponseEntity.ok(ledgerEntryService.findAllLedger());
    }
}
