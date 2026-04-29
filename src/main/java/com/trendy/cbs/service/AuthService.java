package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.request.AuthReq;
import com.trendy.cbs.payload.request.CustomerRequest;
import com.trendy.cbs.payload.request.CustomerSignInRequest;
import com.trendy.cbs.payload.request.SignOutRequest;
import com.trendy.cbs.payload.response.AuthResponse;
import jakarta.validation.Valid;

public interface AuthService {
    AuthResponse signInAsStaff(@Valid AuthReq req);

    CustomerDTO signUpAsCustomer(@Valid CustomerRequest request);

    AuthResponse signInAsCustomer(@Valid CustomerSignInRequest request);

    AuthResponse signOut(SignOutRequest request);
}
