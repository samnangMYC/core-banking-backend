package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.entity.User;
import com.trendy.cbs.enums.*;
import com.trendy.cbs.exception.BusinessException;
import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.dto.SecurityAuditEvent;
import com.trendy.cbs.payload.request.CustomerRegistrationRequest;
import com.trendy.cbs.repos.CustomerRepository;
import com.trendy.cbs.repos.UserRepository;
import com.trendy.cbs.security.CurrentUserProvider;
import com.trendy.cbs.service.CustomerService;
import com.trendy.cbs.service.KeycloakAdminService;
import com.trendy.cbs.service.SecurityAuditService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final SecurityAuditService securityAuditService;
    private final UserServiceImpl userServiceImpl;

    @Override
    @Transactional
    public CustomerDTO createCustomerByMe(CustomerRegistrationRequest req) {

        validateRequest(req);

        String keycloakUserId = null;
        Instant start = Instant.now();

        try {
            UserRepresentation kcUser = buildKeycloakUser(req);

            keycloakUserId = keycloakAdminService.createUser(kcUser);
            keycloakAdminService.setPassword(keycloakUserId, req.getPasscode());
            keycloakAdminService.assignRealmRole(keycloakUserId, SystemRole.CUSTOMER);

            User user = User.builder()
                    .authProvider(AuthProvider.KEYCLOAK)
                    .authProviderUserId(keycloakUserId)
                    .username(req.getPhoneNumber())
                    .email(req.getEmail())
                    .userType(UserType.CUSTOMER)
                    .build();

            Customer customer = Customer.builder()
                    .firstName(req.getFirstName())
                    .lastName(req.getLastName())
                    .gender(req.getGender())
                    .phoneNumber(req.getPhoneNumber())
                    .occupation(req.getOccupation())
                    .nationality(req.getNationality())
                    .maritalStatus(req.getMaritalStatus())
                    .status(CustomerStatus.PENDING)
                    .user(user)
                    .build();

            userRepository.save(user);
            Customer savedCustomer = customerRepository.save(customer);

            securityAuditService.record(
                    SecurityAuditEvent.builder()
                            .userId(String.valueOf(user.getId()))
                            .username(user.getUsername())
                            .method("POST")
                            .path("/api/v1/customer-requests")
                            .statusCode(201)
                            .action("CREATE_CUSTOMER_REQUEST")
                            .result("SUCCESS")
                            .errorMessage(null)
                            .durationMs(Duration.between(start, Instant.now()).toMillis())
                            .occurredAt(Instant.now())
                            .build()
            );

            return CustomerDTO.builder()
                    .customerId(String.valueOf(savedCustomer.getId()))
                    .username(user.getUsername())
                    .firstName(savedCustomer.getFirstName())
                    .lastName(savedCustomer.getLastName())
                    .gender(savedCustomer.getGender())
                    .email(user.getEmail())
                    .phoneNumber(savedCustomer.getPhoneNumber())
                    .occupation(savedCustomer.getOccupation())
                    .nationality(savedCustomer.getNationality())
                    .maritalStatus(savedCustomer.getMaritalStatus())
                    .status(savedCustomer.getStatus())
                    .createdAt(savedCustomer.getCreatedAt())
                    .updatedAt(savedCustomer.getUpdatedAt())
                    .build();

        } catch (Exception ex) {

            if (keycloakUserId != null) {
                keycloakAdminService.deleteUserQuietly(keycloakUserId);
            }

            securityAuditService.record(
                    SecurityAuditEvent.builder()
                            .userId(null)
                            .username(req.getPhoneNumber())
                            .method("POST")
                            .path("/api/v1/customer-requests")
                            .statusCode(500)
                            .action("CREATE_CUSTOMER_REQUEST")
                            .result("FAILED")
                            .errorMessage(ex.getMessage())
                            .durationMs(Duration.between(start, Instant.now()).toMillis())
                            .occurredAt(Instant.now())
                            .build()
            );

            throw new RuntimeException("Failed to create customer request", ex);
        }
    }

    @Override
    public CustomerDTO getCustomerByMe(Jwt jwt) {
        User user = userServiceImpl.loadUserByJwt(jwt);
        return customerRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> BusinessException.notFound(
                        ErrorCode.CuSTOMER_NOT_FOUND,
                        "Customer not found "
                ));
    }

    private UserRepresentation buildKeycloakUser(CustomerRegistrationRequest req) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(req.getPhoneNumber());
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(true);
        return user;
    }

    private void validateRequest(CustomerRegistrationRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BusinessException(
                    "Email already exists",
                    ErrorCode.EMAIL_ALREADY_EXISTS,
                    HttpStatus.CONFLICT.value()
            );
        }

        if (customerRepository.existsByPhoneNumber(req.getPhoneNumber())) {
            throw new BusinessException(
                    "Phone number already exists",
                    ErrorCode.PHONE_ALREADY_EXISTS,
                    HttpStatus.CONFLICT.value()
            );
        }
    }
}
