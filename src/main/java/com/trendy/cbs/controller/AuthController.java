package com.trendy.cbs.controller;

import com.trendy.cbs.payload.request.AuthReq;
import com.trendy.cbs.payload.request.CustomerSignInRequest;
import com.trendy.cbs.payload.request.SignOutRequest;
import com.trendy.cbs.payload.response.AuthResponse;
import com.trendy.cbs.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/api/v1/admin/auth/signin")
    public ResponseEntity<AuthResponse> adminSignIn(
            @Valid @RequestBody AuthReq req) {
        log.info("Admin login attempt username={}", req.getUsername());

        AuthResponse res = authService.signInAsStaff(req);
        log.info("Admin login success username={}", req.getUsername());
        return ResponseEntity.ok(res);


    }

    /**
     * Sign out the currently authenticated user (Admin, Staff, or Customer).
     *
     * <p>This endpoint performs a logout by:
     * <ul>
     *     <li>Invalidating the session in Keycloak using the provided refresh token</li>
     *     <li>Clearing the Spring Security context</li>
     *     <li>Returning a standardized logout response</li>
     * </ul>
     */
    @PostMapping("/api/v1/auth/signout")
    public ResponseEntity<AuthResponse> SignOut(@RequestBody SignOutRequest request){
        return ResponseEntity.ok(authService.signOut(request));
    }

    /**
     * Customer Sign-In
     * Description:
     * Authenticates a customer using phone + passcode.
     */
    @PostMapping("/api/v1/customer/auth/signin")
    public ResponseEntity<AuthResponse> customerSignIn(
            @Valid @RequestBody CustomerSignInRequest req) {

        log.info("Customer login attempt phone={}", req.getPhoneNumber());

        AuthResponse res = authService.signInAsCustomer(req);

        log.info("Customer login success phone={}", req.getPhoneNumber());

        return ResponseEntity.ok(res);
    }

}
