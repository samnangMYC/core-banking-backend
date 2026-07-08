package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.AccountDTO;
import com.trendy.cbs.payload.request.CreateSelfAccountReq;
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

@Slf4j
@RestController
@PreAuthorize("hasRole('CUSTOMER')")
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
    @PostMapping
    public ResponseEntity<AccountDTO> request(@AuthenticationPrincipal Jwt jwt,
                                                       @Valid @RequestBody CreateSelfAccountReq request){
        log.info("Received request to create account {}", request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createSelfAccount(jwt, request));
    }

    @GetMapping
    public ResponseEntity<AccountDTO> selfFetch(@AuthenticationPrincipal Jwt jwt){
        log.info("Received request to get account {}", jwt);
        return ResponseEntity.ok(accountService.getSelfAccountByJwt(jwt));
    }


}
