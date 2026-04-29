package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.request.*;
import com.trendy.cbs.payload.response.AuthResponse;
import jakarta.validation.Valid;

public interface AuthService {
    AuthResponse signInAsStaff(@Valid AuthReq req);

    CustomerDTO signUpAsCustomer(@Valid CustomerRegistrationRequest request);

    AuthResponse signInAsCustomer(@Valid CustomerSignInRequest request);

    AuthResponse signOut(SignOutRequest request);
}
