package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.LedgerEntry;
import com.trendy.cbs.mapper.LedgerEntryMapper;
import com.trendy.cbs.payload.dto.LedgerEntryDTO;
import com.trendy.cbs.repos.LedgerEntryRepository;
import com.trendy.cbs.service.LedgerEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LedgerEntryServiceImpl implements LedgerEntryService {

    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerEntryMapper ledgerEntryMapper;

    @Override
    public List<LedgerEntryDTO> findAllLedger() {
        return ledgerEntryMapper.toListDto(ledgerEntryRepository.findAll());
    }
}
