package com.trendy.cbs.controller.staff;

import com.trendy.cbs.payload.dto.AccountDTO;
import com.trendy.cbs.payload.dto.BalanceDTO;
import com.trendy.cbs.payload.request.AccountStatusReq;
import com.trendy.cbs.payload.request.CreateAccountReq;
import com.trendy.cbs.payload.request.DepositReq;
import com.trendy.cbs.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Staff Accounts", description = "Back-office account management for staff (tellers, supervisors, managers, admins)")
public class AccountController {

    private final AccountService accountService;

    @Operation(
            summary = "Create an account for a customer",
            description = "Opens a new account on behalf of a customer. Restricted to front-line and management staff.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Caller lacks a permitted role", content = @Content)
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','MANAGER','TELLER')")
    @PostMapping
    public ResponseEntity<AccountDTO> createAccounts(@Valid @RequestBody CreateAccountReq req){
        log.info("Received request to Create Accounts for Account");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccountByStaff(req));
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
    @Operation(
            summary = "List all accounts",
            description = "Fetches every account in the system. Restricted to back-office and oversight staff.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Caller lacks a permitted role", content = @Content)
    })
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
    @Operation(
            summary = "Get account by ID",
            description = "Fetches account details for the given internal account ID. Restricted to staff roles that legitimately need to look up any customer's account.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID format", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Caller lacks a permitted role", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','OPERATIONS','MANAGER','BRANCH_MANAGER','ACCOUNTANT','AUDITOR','TELLER')")
    @GetMapping("{id}")
    public ResponseEntity<AccountDTO> findAccountById(
            @Parameter(description = "Internal account ID", required = true, example = "1")
            @PathVariable String id){
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
    @Operation(
            summary = "Get account by account number",
            description = "Fetches account details for the given account number. Restricted to staff roles that legitimately need to look up any customer's account.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Caller lacks a permitted role", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','OPERATIONS','MANAGER','BRANCH_MANAGER','ACCOUNTANT','AUDITOR','TELLER')")
    @GetMapping("/{id}/number")
    public ResponseEntity<AccountDTO> findAccountByAccountNumber(
            @Parameter(description = "Account number to search for", required = true)
            @PathVariable("id") String accNumber){
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
    @Operation(
            summary = "Get account balance",
            description = "Fetches the current and available balance for the given account ID. Restricted to staff roles that legitimately need to view any customer's balance.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BalanceDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID format", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Caller lacks a permitted role", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN','OPERATIONS','MANAGER','BRANCH_MANAGER','ACCOUNTANT','AUDITOR','TELLER')")
    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceDTO> getAccountBalance(
            @Parameter(description = "Internal account ID", required = true, example = "1")
            @PathVariable String id){
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
    @Operation(
            summary = "Update account status",
            description = "Changes an account's lifecycle status (e.g. activate, freeze, close). Restricted to supervisory and management roles authorized to approve such changes.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account status updated successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Caller lacks a permitted role", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @PreAuthorize("hasAnyRole('SUPERVISOR','MANAGER','BRANCH_MANAGER','SYSTEM_ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountDTO> updateAccountStatus(
            @Parameter(description = "Internal account ID", required = true, example = "1")
            @PathVariable String id,
            @Valid @RequestBody AccountStatusReq request){
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
    @Operation(
            summary = "Deposit funds into an account",
            description = "Credits an account's balance. Restricted to front-line and supervisory staff authorized to process cash transactions.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deposit processed successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Caller lacks a permitted role", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @PreAuthorize("hasAnyRole('TELLER','SUPERVISOR','MANAGER','BRANCH_MANAGER','SYSTEM_ADMIN')")
    @PatchMapping("/{id}/deposit")
    public ResponseEntity<AccountDTO> deposit(
            @Parameter(description = "Internal account ID", required = true, example = "1")
            @PathVariable String id,
            @Valid @RequestBody DepositReq request){
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
    @Operation(
            summary = "Withdraw funds from an account",
            description = "Debits an account's balance. Restricted to front-line and supervisory staff authorized to process cash transactions.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Withdrawal processed successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or insufficient available balance", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Caller lacks a permitted role", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @PreAuthorize("hasAnyRole('TELLER','SUPERVISOR','MANAGER','BRANCH_MANAGER','SYSTEM_ADMIN')")
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<AccountDTO> withdraw(
            @Parameter(description = "Internal account ID", required = true, example = "1")
            @PathVariable String id,
            @Valid @RequestBody DepositReq request){
        log.info("Received request to balance withdraw {}", id);
        log.info("Received request to balance withdraw {}", request.toString());
        return ResponseEntity.ok(accountService.withdraw(Long.valueOf(id),request));
    }

    /**
     * Deletes an account by its ID.`
     *
     * This endpoint handles DELETE requests to permanently remove an account. It is restricted to
     * system administrators only, as account deletion is a highly destructive operation.
     *
     * @param id The ID of the account to delete.
     * @return A ResponseEntity with HTTP status 204 (NO_CONTENT) containing a confirmation message.
     */
    @Operation(
            summary = "Delete an account",
            description = "Permanently removes an account. Restricted to system administrators only, as account deletion is a highly destructive operation.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Caller lacks a permitted role", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @DeleteMapping("{id}")
    private ResponseEntity<String> deleteAccountById(
            @Parameter(description = "Internal account ID", required = true, example = "1")
            @PathVariable String id){
        log.info("Received request to delete account by id {}", id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(accountService.deleteAccountById(id));
    }



}
