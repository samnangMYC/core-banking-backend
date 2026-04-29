package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.request.CustomerRegistrationRequest;
import com.trendy.cbs.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    public ResponseEntity<CustomerDTO> request(@Valid @RequestBody CustomerRegistrationRequest request) {
        log.trace("Received request for CustomerRegistrationRequest: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomerByMe(request));

    }

    private ResponseEntity<CustomerDTO> getMe(@AuthenticationPrincipal Jwt jwt) {
        log.trace("Received request for CustomerDTO");
        return ResponseEntity.ok(customerService.getCustomerByMe(jwt));
    }


}
