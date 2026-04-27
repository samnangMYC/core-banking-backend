package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.entity.User;
import com.trendy.cbs.enums.*;
import com.trendy.cbs.exception.BusinessException;
import com.trendy.cbs.jwt.JwtRoleExtractor;
import com.trendy.cbs.payload.request.AuthReq;
import com.trendy.cbs.payload.request.CustomerRequest;
import com.trendy.cbs.payload.request.CustomerSignInRequest;
import com.trendy.cbs.payload.response.AuthResponse;
import com.trendy.cbs.repos.CustomerRepository;
import com.trendy.cbs.repos.UserRepository;
import com.trendy.cbs.service.AuthService;
import com.trendy.cbs.service.KeycloakAdminService;
import com.trendy.cbs.service.keycloak.KeycloakTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final KeycloakTokenService keycloakTokenService;
    private final JwtRoleExtractor jwtRoleExtractor;
    private final UserRepository userRepository;

    @Override
    public AuthResponse signInAsStaff(AuthReq req) {
        return login(req.getUsername(), req.getPassword());
    }

    @Override
    @Transactional
    public AuthResponse signUpAsCustomer(CustomerRequest req) {
        validateCustomerSignup(req);

        String keycloakUserId = null;

        try {
            keycloakUserId = createCustomerInKeycloak(req);

            User user = saveCustomerUser(req, keycloakUserId);
            saveCustomer(req, user);

            return login(req.getPhoneNumber(), req.getPasscode());

        } catch (Exception ex) {
            rollbackKeycloakUser(keycloakUserId);
            throw ex;
        }
    }

    @Override
    public AuthResponse signInAsCustomer(CustomerSignInRequest req) {
        customerRepository.findByPhoneNumber(req.getPhoneNumber())
                .orElseThrow(() -> BusinessException.unauthorized(
                        ErrorCode.AUTH_ERROR,
                        "Invalid phone number or passcode"
                ));

        return login(req.getPhoneNumber(), req.getPasscode());
    }

    private AuthResponse login(String username, String password) {
        try {
            AccessTokenResponse token = keycloakTokenService.login(username, password);
            Set<String> roles = jwtRoleExtractor.extractRoles(token.getToken());

            return AuthResponse.builder()
                    .accessToken(token.getToken())
                    .refreshToken(token.getRefreshToken())
                    .expiresIn(token.getExpiresIn())
                    .tokenType(token.getTokenType())
                    .username(username)
                    .roles(roles)
                    .build();

        } catch (Exception ex) {
            log.warn("Login failed username={}", username);
            throw BusinessException.unauthorized(
                    ErrorCode.AUTH_ERROR,
                    "Username or password is incorrect"
            );
        }
    }

    private String createCustomerInKeycloak(CustomerRequest req) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(req.getPhoneNumber());
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(req.getEmail() != null);

        String userId = keycloakAdminService.createUser(user);
        keycloakAdminService.setPassword(userId, req.getPasscode());
        keycloakAdminService.assignRealmRole(userId, SystemRole.CUSTOMER_SERVICE);

        return userId;
    }

    private User saveCustomerUser(CustomerRequest req, String keycloakUserId) {
        User user = User.builder()
                .authProvider(AuthProvider.KEYCLOAK)
                .authProviderUserId(keycloakUserId)
                .username(req.getPhoneNumber())
                .email(req.getEmail())
                .userType(UserType.CUSTOMER)
                .build();

        return userRepository.save(user);
    }

    private Customer saveCustomer(CustomerRequest req, User user) {
        Customer customer = Customer.builder()
                .user(user)
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .gender(req.getGender())
                .phoneNumber(req.getPhoneNumber())
                .occupation(req.getOccupation())
                .nationality(req.getNationality())
                .maritalStatus(req.getMaritalStatus())
                .status(CustomerStatus.ACTIVE)
                .build();

        return customerRepository.save(customer);
    }

    private void validateCustomerSignup(CustomerRequest req) {
        if (customerRepository.existsByPhoneNumber(req.getPhoneNumber())) {
            throw BusinessException.badRequest(
                    ErrorCode.CUSTOMER_ALREADY_EXISTS,
                    "Phone number is already registered"
            );
        }

        if (req.getEmail() != null && userRepository.existsByEmail(req.getEmail())) {
            throw BusinessException.badRequest(
                    ErrorCode.CUSTOMER_ALREADY_EXISTS,
                    "Email is already registered"
            );
        }
    }

    private void rollbackKeycloakUser(String keycloakUserId) {
        if (keycloakUserId != null) {
            keycloakAdminService.deleteUserQuietly(keycloakUserId);
        }
    }


}
