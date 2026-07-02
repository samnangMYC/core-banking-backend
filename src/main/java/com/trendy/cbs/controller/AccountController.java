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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Creates a new account for the currently authenticated customer.
     *
     * This endpoint handles POST requests to open a self-service account. It validates the incoming
     * request body and delegates the creation logic to the account service using the identity resolved
     * from the JWT. If successful, it returns the created account DTO wrapped in a ResponseEntity with
     * HTTP status 201 (CREATED).
     *
     * @param jwt The authenticated customer's JWT, used to resolve the owning customer.
     * @param request The request body containing the details for the new account. Must be valid and non-null.
     * @return A ResponseEntity containing the created AccountDTO with HTTP status 201 (CREATED).
     */
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<AccountDTO> request(@AuthenticationPrincipal Jwt jwt,
                                                       @Valid @RequestBody AccountRequest request){
        log.info("Received request to create account {}", request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createSelfAccount(jwt, request));
    }

    /**
     * Retrieves all accounts in the system.
     *
     * This endpoint handles GET requests to fetch every account. It is restricted to back-office and
     * oversight staff who need a full view across customers, and delegates the retrieval to the account
     * service.
     *
     * @return A ResponseEntity containing the list of AccountDTO with HTTP status 200 (OK).
     */
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','OPERATIONS','MANAGER','BRANCH_MANAGER','ACCOUNTANT','AUDITOR','TELLER')")
    @GetMapping
    public ResponseEntity<List<AccountDTO>> findAllAccounts(){
        log.info("Received request to find all accounts");
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    /**
     * Retrieves an account by its internal ID.
     *
     * This endpoint handles GET requests to fetch a specific account using its ID. It is restricted to
     * staff roles that legitimately need to look up any customer's account.
     *
     * @param id The ID of the account to retrieve.
     * @return A ResponseEntity containing the AccountDTO with HTTP status 200 (OK).
     */
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','OPERATIONS','MANAGER','BRANCH_MANAGER','ACCOUNTANT','AUDITOR','TELLER')")
    @GetMapping("{id}")
    public ResponseEntity<AccountDTO> findAccountById(@PathVariable String id){
        log.info("Received request to find account by id {}", id);
        return ResponseEntity.ok(accountService.getAccountById(Long.valueOf(id)));
    }

    /**
     * Retrieves an account by its account number.
     *
     * This endpoint handles GET requests to fetch a specific account using its account number. It is
     * restricted to staff roles that legitimately need to look up any customer's account.
     *
     * @param accNumber The account number of the account to retrieve.
     * @return A ResponseEntity containing the AccountDTO with HTTP status 200 (OK).
     */
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','OPERATIONS','MANAGER','BRANCH_MANAGER','ACCOUNTANT','AUDITOR','TELLER')")
    @GetMapping("/{id}/number")
    public ResponseEntity<AccountDTO> findAccountByAccountNumber(@PathVariable("id") String accNumber){
        log.info("Received request to find account by account number {}", accNumber);
        return ResponseEntity.ok(accountService.getAccountByAccountNumber(accNumber));
    }

    /**
     * Retrieves the balance of an account by its ID.
     *
     * This endpoint handles GET requests to fetch the current and available balance of a specific
     * account. It is restricted to staff roles that legitimately need to view any customer's balance.
     *
     * @param id The ID of the account whose balance is requested.
     * @return A ResponseEntity containing the BalanceDTO with HTTP status 200 (OK).
     */
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','OPERATIONS','MANAGER','BRANCH_MANAGER','ACCOUNTANT','AUDITOR','TELLER')")
    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceDTO> getAccountBalance(@PathVariable String id){
        log.info("Received request to get account balance {}", id);
        return ResponseEntity.ok(accountService.getAccountBalance(Long.valueOf(id)));
    }

    /**
     * Updates the status of an account (e.g. activate, freeze, close).
     *
     * This endpoint handles PATCH requests to change an account's lifecycle status. It is restricted to
     * supervisory and management roles authorized to approve such changes.
     *
     * @param id The ID of the account to update.
     * @param request The request body containing the new status details.
     * @return A ResponseEntity containing the updated AccountDTO with HTTP status 200 (OK).
     */
    @PreAuthorize("hasAnyRole('SUPERVISOR','MANAGER','BRANCH_MANAGER','SYSTEM_ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountDTO> updateAccountStatus(@PathVariable String id,@Valid @RequestBody AccountStatusReq request){
        log.info("Received request by account id {}", id);
        log.info("Received request to update account request {}", request);
        return ResponseEntity.ok(accountService.updateAccountStatus(Long.valueOf(id),request));
    }

    /**
     * Deposits funds into an account.
     *
     * This endpoint handles PATCH requests to credit an account's balance. It is restricted to
     * front-line and supervisory staff authorized to process cash transactions.
     *
     * @param id The ID of the account to credit.
     * @param request The request body containing the deposit amount and details.
     * @return A ResponseEntity containing the updated AccountDTO with HTTP status 200 (OK).
     */
    @PreAuthorize("hasAnyRole('TELLER','SUPERVISOR','MANAGER','BRANCH_MANAGER','SYSTEM_ADMIN')")
    @PatchMapping("/{id}/deposit")
    public ResponseEntity<AccountDTO> deposit(@PathVariable String id,@Valid @RequestBody DepositReq request){
        log.info("Received request to balance deposit {}", id);
        log.info("Received request to balance deposit {}", request.toString());
        return ResponseEntity.ok(accountService.deposit(Long.valueOf(id),request));
    }

    /**
     * Withdraws funds from an account.
     *
     * This endpoint handles PATCH requests to debit an account's balance. It is restricted to
     * front-line and supervisory staff authorized to process cash transactions.
     *
     * @param id The ID of the account to debit.
     * @param request The request body containing the withdrawal amount and details.
     * @return A ResponseEntity containing the updated AccountDTO with HTTP status 200 (OK).
     */
    @PreAuthorize("hasAnyRole('TELLER','SUPERVISOR','MANAGER','BRANCH_MANAGER','SYSTEM_ADMIN')")
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@PathVariable String id,@Valid @RequestBody DepositReq request){
        log.info("Received request to balance withdraw {}", id);
        log.info("Received request to balance withdraw {}", request.toString());
        return ResponseEntity.ok(accountService.withdraw(Long.valueOf(id),request));
    }

    /**
     * Deletes an account by its ID.
     *
     * This endpoint handles DELETE requests to permanently remove an account. It is restricted to
     * system administrators only, as account deletion is a highly destructive operation.
     *
     * @param id The ID of the account to delete.
     * @return A ResponseEntity with HTTP status 204 (NO_CONTENT) containing a confirmation message.
     */
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @DeleteMapping("{id}")
    private ResponseEntity<String> deleteAccountById(@PathVariable String id){
        log.info("Received request to delete account by id {}", id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(accountService.deleteAccountById(id));
    }
}
