package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.request.CustomerRegistrationRequest;
import com.trendy.cbs.payload.request.CustomerStatusRequest;
import com.trendy.cbs.payload.request.CustomerUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomerByMe(@Valid CustomerRegistrationRequest request);

    CustomerDTO getCustomerByMe(Jwt jwt);

    CustomerDTO updateCustomerByMe(Jwt jwt, CustomerUpdateRequest request);

    List<CustomerDTO> getAllCustomer();

    CustomerDTO approveCustomer(Long customerId);

    CustomerDTO rejectCustomer(Long customerId);

    CustomerDTO suspendCustomer(Long customerId);
    CustomerDTO updateCustomerStatus(Long customerId, CustomerStatusRequest request);
}
