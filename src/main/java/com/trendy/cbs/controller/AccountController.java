package com.trendy.cbs.controller;

import com.trendy.cbs.enums.AccountStatus;
import com.trendy.cbs.payload.dto.AccountDTO;
import com.trendy.cbs.payload.request.AccountRequest;
import com.trendy.cbs.payload.request.AccountStatusReq;
import com.trendy.cbs.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDTO> createNewAccount( @RequestHeader("X-Customer-Id") Long customerId,
                                                        @Valid @RequestBody AccountRequest request){
        log.info("Received request to create account {}", request);
        return ResponseEntity.ok(accountService.createNewAccount(customerId,request));
    }

    // LIST: Paginated List of Accounts
    @GetMapping
    public ResponseEntity<List<AccountDTO>> findAllAccounts(){
        log.info("Received request to find all accounts");
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    // READ: Get Account by ID
    @GetMapping("{id}")
    public ResponseEntity<AccountDTO> findAccountById(@PathVariable String id){
        log.info("Received request to find account by id {}", id);
        return ResponseEntity.ok(accountService.getAccountById(Long.valueOf(id)));
    }
    // UPDATE: Update Account Status
    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountDTO> updateAccountStatus(@PathVariable String id, @RequestBody AccountStatusReq request){
        log.info("Received request to update account request {}", request);
        return ResponseEntity.ok(accountService.updateAccountStatus(Long.valueOf(id),request));
    }
    // DELETE: Soft Delete Account
    // READ: get balance
}
