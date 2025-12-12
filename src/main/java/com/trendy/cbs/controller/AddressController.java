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
@RequestMapping("/api/v1/customer/me/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;


    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestHeader("X-Customer-Id") Long userId,
                                                    @Valid @RequestBody AddressRequest request) {
         log.info("Received request to create address : {}", request);
         return ResponseEntity.ok(addressService.createAddress(userId,request));
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddress() {
        log.info("Received request to get all address...");
        return ResponseEntity.ok(addressService.getAllAddress());
    }

    @GetMapping("{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id) {
        log.info("Received request to get address by id : {}", id);
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id,@Valid @RequestBody AddressRequest request) {
        log.info("Received request to update address : {}", request);
        return ResponseEntity.ok(addressService.updateAddress(id,request));
    }


    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUserAddress(@PathVariable Long id) {
        log.info("Received request to delete address : {}", id);
        return ResponseEntity.ok(addressService.deleteAddressById(id));
    }


}
