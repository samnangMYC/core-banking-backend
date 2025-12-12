package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.request.CustomerRequest;
import com.trendy.cbs.payload.request.CustomerStatusRequest;
import com.trendy.cbs.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @PostMapping
    public ResponseEntity<CustomerDTO> createNewCustomer(@Valid @RequestBody CustomerRequest customerRequest){
        log.info("Creating customer {}", customerRequest.toString());
        return ResponseEntity.ok(customerService.createNewCustomer(customerRequest));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(){
        log.info("Retrieving all customers.....");
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Optional<CustomerDTO>> getCustomerById(@PathVariable Long id){
        log.info("Retrieving customer {}", id.toString());
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequest customerRequest){
        log.info("Updating customer: {}", customerRequest.toString());
        return ResponseEntity.ok(customerService.updateCustomer(id, customerRequest));
    }

    @PatchMapping("{id}")
    public ResponseEntity<CustomerDTO> updateCustomerDocStatus(@PathVariable Long id, @Valid @RequestBody CustomerStatusRequest customerStatusRequest){
        log.info("Updating customer status {}", customerStatusRequest.toString());
        return ResponseEntity.ok(customerService.updateCustomerStatus(id, customerStatusRequest));
    }



}
