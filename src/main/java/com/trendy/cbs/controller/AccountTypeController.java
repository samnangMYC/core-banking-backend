package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.AccountTypeDTO;
import com.trendy.cbs.payload.request.AccountTypeRequest;
import com.trendy.cbs.service.AccountTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.jspecify.annotations.NonNull;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/account-type")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    /**
     * Creates a new account type based on the provided request.
     *
     * This endpoint handles POST requests to create an account type. It validates the incoming request body
     * and delegates the creation logic to the account type service. If successful, it returns the created
     * account type DTO wrapped in a ResponseEntity with HTTP status 200 (OK).
     *
     * @param request The request body containing the details for the new account type. Must be valid and non-null.
     * @return A ResponseEntity containing the created AccountTypeDTO with HTTP status 200 (OK).
     */
    @PostMapping
    public ResponseEntity<@NonNull AccountTypeDTO> createAccountType(@Valid @RequestBody AccountTypeRequest request){
        log.info("Received request to create account type : {}", request);
        return ResponseEntity.ok(accountTypeService.createAccountType(request));

    }

    /**
     * Retrieves all account types.
     *
     * This endpoint handles GET requests to fetch a list of all account types. It logs the request and
     * delegates the retrieval to the account type service, returning the list of AccountTypeDTOs
     * wrapped in a ResponseEntity with HTTP status 200 (OK).
     *
     * @return A ResponseEntity containing a non-null list of AccountTypeDTOs with HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<@NonNull List<AccountTypeDTO>> getAllAccountTypes(){
        log.info("Received request to get all account-types");
        return ResponseEntity.ok(accountTypeService.getAllAccountTypes());
    }

    /**
     * Retrieves an account type by its ID.
     *
     * This endpoint handles GET requests to fetch a specific account type using the provided ID.
     * It logs the request and delegates the retrieval to the account type service, returning
     * the AccountTypeDTO wrapped in a ResponseEntity with HTTP status 200 (OK).
     *
     * @param id The ID of the account type to retrieve.
     * @return A ResponseEntity containing the non-null AccountTypeDTO with HTTP status 200 (OK).
     */
    @GetMapping("{id}")
    public ResponseEntity<@NonNull AccountTypeDTO> getAccountTypeById(@PathVariable Long id){
        log.info("Received request to get account type by id : {}", id);
        return ResponseEntity.ok(accountTypeService.getAccountTypeById(id));
    }

    /**
     * Updates an existing account type.
     *
     * This endpoint handles PUT requests to update an account type using the provided ID and request body.
     * It logs the request and delegates the update to the account type service, returning the updated
     * AccountTypeDTO wrapped in a ResponseEntity with HTTP status 200 (OK).
     *
     * @param id The ID of the account type to update.
     * @param request The request body containing the updated details for the account type.
     * @return A ResponseEntity containing the non-null updated AccountTypeDTO with HTTP status 200 (OK).
     */
    @PutMapping("{id}")
    public ResponseEntity<@NonNull AccountTypeDTO> updateAccountType(@PathVariable Long id, @RequestBody AccountTypeRequest request){
        log.info("Received request to update account type : {}", request);
        return ResponseEntity.ok(accountTypeService.updateAccountType(id,request));
    }

    /**
     * Deletes an account type by its ID.
     *
     * This endpoint handles DELETE requests to remove a specific account type using the provided ID.
     * It logs the request and delegates the deletion to the account type service, returning a success
     * message wrapped in a ResponseEntity with HTTP status 200 (OK).
     *
     * @param id The ID of the account type to delete.
     * @return A ResponseEntity containing a non-null success message with HTTP status 200 (OK).
     */
    @DeleteMapping("{id}")
    public ResponseEntity<@NonNull String> deleteAccountType(@PathVariable Long id){
        log.info("Received request to delete account type by id : {}", id);
        return ResponseEntity.ok(accountTypeService.deleteAccountTypeById(id));
    }

}
