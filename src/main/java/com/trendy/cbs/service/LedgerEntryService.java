package com.trendy.cbs.service;

import com.trendy.cbs.entity.LedgerEntry;
import com.trendy.cbs.payload.dto.LedgerEntryDTO;

import java.util.List;

public interface LedgerEntryService {
    List<LedgerEntryDTO> findAllLedger();
}
