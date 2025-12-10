package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.request.CustomerRequest;
import com.trendy.cbs.payload.request.CustomerStatusRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    CustomerDTO createNewCustomer(@Valid CustomerRequest customerRequest);

    List<CustomerDTO> getAllCustomers();

    Optional<CustomerDTO> getCustomerById(Long id);

    CustomerDTO updateCustomer(Long id, @Valid CustomerRequest customerRequest);

    CustomerDTO updateCustomerStatus(Long id, @Valid CustomerStatusRequest customerStatusRequest);
}
