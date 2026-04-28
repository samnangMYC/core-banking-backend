package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.request.CustomerRequest;
import com.trendy.cbs.repos.CustomerRepository;
import com.trendy.cbs.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;




}
