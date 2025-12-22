package com.trendy.cbs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/fund-transfer")
@RequiredArgsConstructor
public class FundTransferController {
    // generate transactionReference
    // check from and to account (sender,receiver)
    // get amount and currency_id
    // get purpose
    // set TransferType and conditional
}
