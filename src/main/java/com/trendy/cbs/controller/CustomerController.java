package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.request.CustomerRegistrationRequest;
import com.trendy.cbs.payload.request.CustomerStatusRequest;
import com.trendy.cbs.payload.request.CustomerUpdateRequest;
import com.trendy.cbs.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.ws.rs.PathParam;
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
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/request")
    public ResponseEntity<CustomerDTO> request(@Valid @RequestBody CustomerRegistrationRequest request) {
        log.info("Received request for CustomerRegistrationRequest: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomerByMe(request));

    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/me")
    public ResponseEntity<CustomerDTO> getMe(@AuthenticationPrincipal Jwt jwt) {
        log.info("Received request for CustomerDTO");
        return ResponseEntity.ok(customerService.getCustomerByMe(jwt));
    }

    @PreAuthorize("hasAnyRole('TELLER','ACCOUNTANT','SYSTEM_ADMIN')")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        log.info("Received request all customers");

        return ResponseEntity.ok(customerService.getAllCustomer());

    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PatchMapping("/me")
    public ResponseEntity<CustomerDTO> updateMe(@AuthenticationPrincipal Jwt jwt,
                                                @Valid @RequestBody CustomerUpdateRequest request) {
       log.info("Received update request");
        return ResponseEntity.ok(customerService.updateCustomerByMe(jwt,request));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR','MANAGER','BRANCH_MANAGER','SYSTEM_ADMIN')")
    @PatchMapping("/{id}/approve")
    public ResponseEntity<CustomerDTO> approve(@PathVariable("id") Long customerId) {
        log.info("Received approve request for customer");
        return ResponseEntity.ok(customerService.approveCustomer(customerId));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR','MANAGER','BRANCH_MANAGER','SYSTEM_ADMIN')")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<CustomerDTO> reject(@PathVariable("id") Long customerId) {
        log.info("Received reject request for customer");
        return ResponseEntity.ok(customerService.rejectCustomer(customerId));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR','MANAGER','AUDITOR','SYSTEM_ADMIN')")
    @PatchMapping("/{id}/suspend")
    public ResponseEntity<CustomerDTO> suspend(@PathVariable("id") Long customerId) {
        log.info("Received suspend user request for customer");
        return ResponseEntity.ok(customerService.suspendCustomer(customerId));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR','MANAGER','BRANCH_MANAGER','OPERATIONS','SYSTEM_ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomerDTO> staus(@PathVariable("id") Long customerId,
                                             @Valid @RequestBody CustomerStatusRequest request) {
        log.info("Received staus user request for customer");
        return ResponseEntity.ok(customerService.updateCustomerStatus(customerId,request));
    }

}
