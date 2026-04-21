package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.AccountDTO;
import com.trendy.cbs.payload.dto.BalanceDTO;
import com.trendy.cbs.payload.request.AccountRequest;
import com.trendy.cbs.payload.request.AccountStatusReq;
import com.trendy.cbs.payload.request.DepositReq;
import com.trendy.cbs.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createNewAccount(customerId, request));
    }

    // LIST: List of All Accounts
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

    @GetMapping("/{accNumber}/number")
    public ResponseEntity<AccountDTO> findAccountByAccountNumber(@PathVariable String accNumber){
        log.info("Received request to find account by account number {}", accNumber);
        return ResponseEntity.ok(accountService.getAccountByAccountNumber(accNumber));
    }

    // READ: get balance by ID
    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceDTO> getAccountBalance(@PathVariable String id){
        log.info("Received request to get account balance {}", id);
        return ResponseEntity.ok(accountService.getAccountBalance(Long.valueOf(id)));
    }

    // UPDATE: Update Account Status
    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountDTO> updateAccountStatus(@PathVariable String id,@Valid @RequestBody AccountStatusReq request){
        log.info("Received request by account id {}", id);
        log.info("Received request to update account request {}", request);
        return ResponseEntity.ok(accountService.updateAccountStatus(Long.valueOf(id),request));
    }

    // UPDATE: Deposit Account Balance By accId
    @PatchMapping("/{id}/deposit")
    public ResponseEntity<AccountDTO> deposit(@PathVariable String id,@Valid @RequestBody DepositReq request){
        log.info("Received request to balance deposit {}", id);
        log.info("Received request to balance deposit {}", request.toString());
        return ResponseEntity.ok(accountService.deposit(Long.valueOf(id),request));
    }

    // UPDATE: withdraw Account Balance By accId
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@PathVariable String id,@Valid @RequestBody DepositReq request){
        log.info("Received request to balance withdraw {}", id);
        log.info("Received request to balance withdraw {}", request.toString());
        return ResponseEntity.ok(accountService.withdraw(Long.valueOf(id),request));
    }

    // DELETE: delete account by accId for admin only
    @DeleteMapping("{id}")
    private ResponseEntity<String> deleteAccountById(@PathVariable String id){
        log.info("Received request to delete account by id {}", id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(accountService.deleteAccountById(id));
    }
}
