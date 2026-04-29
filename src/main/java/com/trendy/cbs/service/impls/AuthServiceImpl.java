package com.trendy.cbs.service.impls;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.entity.User;
import com.trendy.cbs.enums.*;
import com.trendy.cbs.exception.BusinessException;
import com.trendy.cbs.payload.request.*;
import com.trendy.cbs.security.CurrentUserProvider;
import com.trendy.cbs.security.JwtRoleExtractor;
import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.dto.SecurityAuditEvent;
import com.trendy.cbs.payload.response.AuthResponse;
import com.trendy.cbs.repos.CustomerRepository;
import com.trendy.cbs.repos.UserRepository;
import com.trendy.cbs.service.AuthService;
import com.trendy.cbs.service.KeycloakAdminService;
import com.trendy.cbs.service.SecurityAuditService;
import com.trendy.cbs.service.keycloak.KeycloakTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
    private final SecurityAuditService auditServices;
    private final CurrentUserProvider currentUserProvider;
    private final SecurityAuditService securityAuditService;

    @Override
    public AuthResponse signInAsStaff(AuthReq req) {
        return login(req.getUsername(), req.getPassword());
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

    @Override
    public AuthResponse signOut(SignOutRequest request) {

        String username = currentUserProvider.getCurrentUsername();
        String userId = currentUserProvider.getCurrentUserId();

        try {
            keycloakTokenService.logout(request.getRefreshToken());

            SecurityContextHolder.clearContext();

            securityAuditService.record(
                    SecurityAuditEvent.builder()
                            .userId(userId)
                            .username(username)
                            .action("SIGN_OUT")
                            .method("POST")
                            .path("/api/v1/customer/auth/signout")
                            .statusCode(201)
                            .action("CREATE_CUSTOMER")
                            .result("SUCCESS")
                            .errorMessage(null)
                            .durationMs(0L)
                            .occurredAt(Instant.now())
                            .build()
            );

            return AuthResponse.builder()
                    .accessToken(null)
                    .refreshToken(null)
                    .expiresIn(0L)
                    .tokenType(null)
                    .username(username)
                    .roles(Set.of())
                    .build();


        } catch (Exception ex) {

            throw new RuntimeException("Failed to sign out");
        }

    }

    private AuthResponse login(String username, String password) {
        try {
            AccessTokenResponse token = keycloakTokenService.login(username, password);

            // Decode token to get user info
            DecodedJWT decoded = JWT.decode(token.getToken());

            String userId = decoded.getSubject();
            String preferredUsername = decoded.getClaim("preferred_username").asString();

            Set<String> roles = jwtRoleExtractor.extractRoles(token.getToken());

            auditServices.record(
                    SecurityAuditEvent.builder()
                            .userId(userId)
                            .username(preferredUsername)
                            .action("SIGN_IN")
                            .result("SUCCESS")
                            .occurredAt(Instant.now())
                            .build()
            );

            return AuthResponse.builder()
                    .accessToken(token.getToken())
                    .refreshToken(token.getRefreshToken())
                    .expiresIn(token.getExpiresIn())
                    .tokenType(token.getTokenType())
                    .username(username)
                    .roles(roles)
                    .build();

        } catch (Exception ex) {
            log.warn("Login failed username={}", username,ex);

            throw BusinessException.unauthorized(
                    ErrorCode.AUTH_ERROR,
                    "Username or password is incorrect"
            );
        }
    }


}
