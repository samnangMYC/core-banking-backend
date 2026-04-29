package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.request.CustomerRegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

public interface CustomerService {
    CustomerDTO createCustomerByMe(@Valid CustomerRegistrationRequest request);

    CustomerDTO getCustomerByMe(Jwt jwt);
}
