package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.entity.User;
import com.trendy.cbs.enums.*;
import com.trendy.cbs.exception.BusinessException;
import com.trendy.cbs.mapper.CustomerMapper;
import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.dto.SecurityAuditEvent;
import com.trendy.cbs.payload.request.CustomerRegistrationRequest;
import com.trendy.cbs.payload.request.CustomerStatusRequest;
import com.trendy.cbs.payload.request.CustomerUpdateRequest;
import com.trendy.cbs.repos.CustomerRepository;
import com.trendy.cbs.repos.UserRepository;
import com.trendy.cbs.service.CustomerService;
import com.trendy.cbs.service.KeycloakAdminService;
import com.trendy.cbs.service.SecurityAuditService;
import com.trendy.cbs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomersServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final SecurityAuditService securityAuditService;
    private final UserService userService;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
                            .path("/api/v1/customer/requests")
                            .statusCode(201)
                            .action("CREATE_CUSTOMER_REQUEST")
                            .result("SUCCESS")
                            .errorMessage(null)
                            .durationMs(Duration.between(start, Instant.now()).toMillis())
                            .occurredAt(Instant.now())
                            .build()
            );

            return customerMapper.toDto(customer);

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
        User user = userService.loadUserByJwt(jwt);
        Customer customer = customerRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> BusinessException.notFound(
                        ErrorCode.CUSTOMER_NOT_FOUND,
                        "Customer not found "
                ));

        return customerMapper.toDto(customer);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomerByMe(Jwt jwt, CustomerUpdateRequest request) {
        User user = userService.loadUserByJwt(jwt);

        Customer customer = customerRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> BusinessException.notFound(
                        ErrorCode.CUSTOMER_NOT_FOUND,
                        "Customer not found"
                ));

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setGender(request.getGender());
        customer.setOccupation(request.getOccupation());
        customer.setNationality(request.getNationality());
        customer.setMaritalStatus(request.getMaritalStatus());

        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toDto(savedCustomer);
    }

    @Override
    public List<CustomerDTO> getAllCustomer() {
        return customerMapper.toListDto(customerRepository.findAll());
    }

    @Override
    public CustomerDTO approveCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    customer.setStatus(CustomerStatus.ACTIVE);

                    return customerMapper.toDto(customer);
                }).orElseThrow(() -> BusinessException.notFound(
                        ErrorCode.CUSTOMER_NOT_FOUND,
                        "Customer not found with id: " + customerId
                ));
    }

    @Override
    public CustomerDTO rejectCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    customer.setStatus(CustomerStatus.PENDING);

                    return customerMapper.toDto(customer);
                }).orElseThrow(() -> BusinessException.notFound(
                        ErrorCode.CUSTOMER_NOT_FOUND,
                        "Customer not found with id: " + customerId
                ));
    }

    @Override
    public CustomerDTO suspendCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    customer.setStatus(CustomerStatus.SUSPENDED);

                    return customerMapper.toDto(customer);
                }).orElseThrow(() -> BusinessException.notFound(
                        ErrorCode.CUSTOMER_NOT_FOUND,
                        "Customer not found with id: " + customerId
                ));
    }

    @Override
    public CustomerDTO updateCustomerStatus(Long customerId,CustomerStatusRequest request) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    customer.setStatus(request.getStatus());

                    customerRepository.save(customer);

                    return customerMapper.toDto(customer);
                }).orElseThrow(() -> BusinessException.notFound(
                        ErrorCode.CUSTOMER_NOT_FOUND,
                        "Customer not found with id: " + customerId
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
