package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.FundTransferDTO;
import com.trendy.cbs.payload.request.FundTransferRequest;
import com.trendy.cbs.payload.request.ReverseFundTransferReq;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface FundTransferService {
    FundTransferDTO createFundTransfer(@Valid FundTransferRequest req);

    List<FundTransferDTO> getAllFundTransfer();

    FundTransferDTO reverseFundTransfer(ReverseFundTransferReq req);
}
