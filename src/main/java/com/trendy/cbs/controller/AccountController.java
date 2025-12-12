package com.trendy.cbs.controller;

import com.trendy.cbs.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

//    @PostMapping
//    public ResponseEntity<?> createNewAccount(AccountRequest request){
//        log.info("Received request to create account {}", request);
//        return ResponseEntity.ok(accountService.createNewAccount(request));
//    }

}
