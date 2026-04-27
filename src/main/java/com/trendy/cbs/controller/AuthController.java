package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.request.AuthReq;
import com.trendy.cbs.payload.request.CustomerRequest;
import com.trendy.cbs.payload.request.CustomerSignInRequest;
import com.trendy.cbs.payload.response.AuthResponse;
import com.trendy.cbs.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Admin (Back-Office) Sign-In
     * Description:
     * Authenticates an internal user (admin/staff) using username + password.
     */
    @PostMapping("/api/v1/admin/auth/sign-in")
    public ResponseEntity<AuthResponse> adminSignIn(
            @Valid @RequestBody AuthReq req) {
        log.info("Admin login attempt username={}", req.getUsername());

        AuthResponse res = authService.signInAsStaff(req);
        log.info("Admin login success username={}", req.getUsername());
        return ResponseEntity.ok(res);
    }

    /**
     * Customer Sign-Up
     * Description:
     * Registers a new customer using phone + passcode.
     * Automatically logs in the user after successful registration.
     */
    @PostMapping("/api/v1/customer/auth/sign-up")
    public ResponseEntity<AuthResponse> customerSignUp(
            @Valid @RequestBody CustomerRequest req) {

        log.info("Customer signup attempt phone={}", req.getPhoneNumber());

        AuthResponse res = authService.signUpAsCustomer(req);

        log.info("Customer signup success phone={}", req.getPhoneNumber());

        return ResponseEntity.ok(res);
    }


    /**
     * Customer Sign-In
     * Description:
     * Authenticates a customer using phone + passcode.
     */
    @PostMapping("/api/v1/customer/auth/sign-in")
    public ResponseEntity<AuthResponse> customerSignIn(
            @Valid @RequestBody CustomerSignInRequest req) {

        log.info("Customer login attempt phone={}", req.getPhoneNumber());

        AuthResponse res = authService.signInAsCustomer(req);

        log.info("Customer login success phone={}", req.getPhoneNumber());

        return ResponseEntity.ok(res);
    }



}
