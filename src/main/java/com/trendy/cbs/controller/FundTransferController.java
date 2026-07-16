package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.FundTransferDTO;
import com.trendy.cbs.payload.request.FundTransferRequest;
import com.trendy.cbs.payload.request.ReverseFundTransferReq;
import com.trendy.cbs.service.FundTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/fund-transfer")
@RequiredArgsConstructor
public class FundTransferController {

    private final FundTransferService fundTransferService;

    @PostMapping
    public ResponseEntity<FundTransferDTO> create(@Valid @RequestBody FundTransferRequest req) {
        log.info("Received request to create account {}", req.toString());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fundTransferService.createFundTransfer(req));
    }

    @GetMapping
    public ResponseEntity<List<FundTransferDTO>> getAll() {
        log.info("Received request to get all fund transfers");
        return ResponseEntity.ok(fundTransferService.getAllFundTransfer());
    }

    @PostMapping("/reverse")
    public ResponseEntity<FundTransferDTO> reverse(@Valid @RequestBody ReverseFundTransferReq req){
        log.info("Received request to reverse fund transfer with {}", req.toString());

        return ResponseEntity.ok(fundTransferService.reverseFundTransfer(req));
    }


}
