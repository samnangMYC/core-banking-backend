package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.AddressDTO;
import com.trendy.cbs.payload.request.AddressRequest;
import com.trendy.cbs.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/me/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * Creates a new user address based on the provided request.
     *
     * @param request the address request containing details for the new address
     * @return ResponseEntity containing the created AddressDTO with HTTP status OK
     */
    @PostMapping
    public ResponseEntity<@NonNull AddressDTO> createUserAddress(@RequestHeader("X-User-Id") Long userId,
                                                                 @Valid @RequestBody AddressRequest request) {

         log.info("Received request to create address : {}", request);
         return ResponseEntity.ok(addressService.createUserAddress(userId,request));
    }

    /**
     * Retrieves all user addresses.
     *
     * @return ResponseEntity containing a list of all AddressDTOs with HTTP status OK
     */
    @GetMapping
    public ResponseEntity<@NonNull List<AddressDTO>> getAllUserAddress() {
        log.info("Received request to get all address...");
        return ResponseEntity.ok(addressService.getAllUserAddress());
    }

    /**
     * Retrieves a user address by its ID.
     *
     * @param id the ID of the address to retrieve
     * @return ResponseEntity containing the AddressDTO if found, with HTTP status OK
     */
    @GetMapping("{id}")
    public ResponseEntity<@NonNull AddressDTO> getUserAddressById(@PathVariable Long id) {
        log.info("Received request to get address by id : {}", id);
        return ResponseEntity.ok(addressService.getUserAddressById(id));
    }

    /**
     * Updates an existing user address based on the provided ID and request.
     *
     * @param id the ID of the address to update
     * @param request the UpdateAddressRequest containing the updated address details
     * @return ResponseEntity containing the updated AddressDTO with HTTP status OK
     */
    @PutMapping("{id}")
    public ResponseEntity<@NonNull AddressDTO> updateUserAddress(@PathVariable Long id,@Valid @RequestBody AddressRequest request) {
        log.info("Received request to update address : {}", request);
        return ResponseEntity.ok(addressService.updateUserAddress(id,request));
    }

    /**
     * Deletes a user address by its ID.
     *
     * @param id the ID of the address to delete
     * @return ResponseEntity containing a success message with HTTP status OK
     */
    @DeleteMapping("{id}")
    public ResponseEntity<@NonNull String> deleteUserAddress(@PathVariable Long id) {
        log.info("Received request to delete address : {}", id);
        return ResponseEntity.ok(addressService.deleteUserAddressById(id));
    }


}
