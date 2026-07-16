package com.trendy.cbs.controller.customer;

import com.trendy.cbs.payload.dto.AccountDTO;
import com.trendy.cbs.payload.dto.BalanceDTO;
import com.trendy.cbs.payload.request.CreateSelfAccountReq;
import com.trendy.cbs.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer/accounts")
@RequiredArgsConstructor
public class CustomerAccountController {

    private final AccountService accountService;

    /**
     * Creates a new account for the authenticated user.
     * <p>
     * The account owner is derived from the authenticated JWT principal rather than
     * from the request body, ensuring a user can only create an account for themselves.
     *
     * @param jwt     the authenticated user's JWT, injected by Spring Security
     * @param request the account creation payload containing the details required to open the account
     * @return a {@link ResponseEntity} containing the created {@link AccountDTO} with HTTP 201 status
     */
    @Operation(
            summary = "Create a self account",
            description = "Creates a new account owned by the currently authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication token", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AccountDTO> create(@AuthenticationPrincipal Jwt jwt,
                                             @Valid @RequestBody CreateSelfAccountReq request) {
        log.info("Received request to create account {}", request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createSelfAccount(jwt, request));
    }

    /**
     * Retrieves the account belonging to the currently authenticated user.
     *
     * @param jwt the authenticated user's JWT, injected by Spring Security
     * @return a {@link ResponseEntity} containing the requesting user's {@link AccountDTO}
     */
    @Operation(
            summary = "Get the authenticated user's account",
            description = "Fetches account details for the account owned by the currently authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid authentication token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<AccountDTO> fetch(@AuthenticationPrincipal Jwt jwt) {
        log.info("Received request to get account by jwt {}", jwt);
        return ResponseEntity.ok(accountService.getSelfAccountByJwt(jwt));
    }

    /**
     * Retrieves an account by its internal identifier.
     *
     * @param id the internal account identifier
     * @return a {@link ResponseEntity} containing the matching {@link AccountDTO}
     */
    @Operation(
            summary = "Get account by ID",
            description = "Fetches account details for the given internal account ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("{id}")
    public ResponseEntity<AccountDTO> findAccountById(
            @Parameter(description = "Internal account ID", required = true, example = "1")
            @PathVariable String id) {
        log.info("Received request to find account by id {}", id);
        return ResponseEntity.ok(accountService.getAccountById(Long.valueOf(id)));
    }

    /**
     * Retrieves an account by its account number.
     *
     * @param accNumber the account number to search for
     * @return a {@link ResponseEntity} containing the matching {@link AccountDTO}
     */
    @Operation(
            summary = "Get account by account number",
            description = "Fetches account details for the given account number.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("/{id}/number")
    public ResponseEntity<AccountDTO> findAccountByAccountNumber(
            @Parameter(description = "Account number to search for", required = true)
            @PathVariable("id") String accNumber) {
        log.info("Received request to find account by account number {}", accNumber);
        return ResponseEntity.ok(accountService.getAccountByAccountNumber(accNumber));
    }

    /**
     * Retrieves the current balance for a given account.
     *
     * @param id the internal account identifier
     * @return a {@link ResponseEntity} containing the account's {@link BalanceDTO}
     */
    @Operation(
            summary = "Get account balance",
            description = "Fetches the current balance for the given account ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BalanceDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)
    })
    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceDTO> getAccountBalance(
            @Parameter(description = "Internal account ID", required = true, example = "1")
            @PathVariable String id) {
        log.info("Received request to get account balance {}", id);
        return ResponseEntity.ok(accountService.getAccountBalance(Long.valueOf(id)));
    }
}
