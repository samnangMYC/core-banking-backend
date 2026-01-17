package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.FundTransferDTO;
import com.trendy.cbs.payload.request.FundTransferRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface FundTransferService {
    FundTransferDTO createFundTransfer(@Valid FundTransferRequest req);

    List<FundTransferDTO> getAllFundTransfer();
}
