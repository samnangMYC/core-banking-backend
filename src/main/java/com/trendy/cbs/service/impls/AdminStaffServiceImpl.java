package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Staff;
import com.trendy.cbs.entity.User;
import com.trendy.cbs.enums.AuthProvider;
import com.trendy.cbs.enums.EmploymentStatus;
import com.trendy.cbs.enums.ErrorCode;
import com.trendy.cbs.enums.UserType;
import com.trendy.cbs.exception.BusinessException;
import com.trendy.cbs.payload.dto.AdminStaffDTO;
import com.trendy.cbs.payload.dto.SecurityAuditEvent;
import com.trendy.cbs.payload.request.AdminStaffReq;
import com.trendy.cbs.repos.StaffRepository;
import com.trendy.cbs.repos.UserRepository;
import com.trendy.cbs.security.CurrentUserProvider;
import com.trendy.cbs.service.AdminStaffService;
import com.trendy.cbs.service.KeycloakAdminService;
import com.trendy.cbs.service.SecurityAuditService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminStaffServiceImpl implements AdminStaffService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final SecurityAuditService  securityAuditService;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public AdminStaffDTO createStaff(AdminStaffReq req) {

        // user validation
        validateRequest(req);

        String keycloakUserId = null;

        try {
            UserRepresentation kcUser = buildKeycloakUser(req);

            keycloakUserId = keycloakAdminService.createUser(kcUser);
            keycloakAdminService.setPassword(keycloakUserId, req.getPassword());
            keycloakAdminService.assignRealmRole(keycloakUserId, req.getSystemRole());

            User user = User.builder()
                    .authProvider(AuthProvider.KEYCLOAK)
                    .authProviderUserId(keycloakUserId)
                    .username(req.getUsername())
                    .email(req.getEmail())
                    .userType(UserType.STAFF)
                    .build();

            Staff staff = Staff.builder()
                    .staffCode(generateStaffCode())
                    .user(user)
                    .firstName(req.getFirstName())
                    .lastName(req.getLastName())
                    .gender(req.getGender())
                    .email(req.getEmail())
                    .phoneNumber(req.getPhoneNumber())
                    .dateOfBirth(req.getDateOfBirth())
                    .nationality(req.getNationality())
                    .maritalStatus(req.getMaritalStatus())
                    .systemRole(req.getSystemRole())
                    .employmentStatus(EmploymentStatus.ACTIVE)
                    .department(req.getDepartment())
                    .jobTitle(req.getJobTitle())
                    .hireDate(req.getHireDate())
                    .salary(req.getSalary())
                    .build();

            user.setStaff(staff);

            userRepository.save(user);

            staff = staffRepository.save(staff);

            securityAuditService.record(
                    SecurityAuditEvent.builder()
                            .userId(String.valueOf(user.getId()))
                            .username(user.getUsername())
                            .method("POST")
                            .path("/api/v1/admin/staff")
                            .statusCode(201)
                            .action("CREATE_STAFF")
                            .result("SUCCESS")
                            .errorMessage(null)
                            .durationMs(0L)
                            .occurredAt(Instant.now())
                            .build()
            );

            return AdminStaffDTO.builder()
                    .userId(user.getId())
                    .staffId(staff.getId())
                    .keycloakUserId(user.getAuthProviderUserId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .staffCode(staff.getStaffCode())
                    .firstName(staff.getFirstName())
                    .lastName(staff.getLastName())
                    .systemRole(staff.getSystemRole())
                    .employmentStatus(staff.getEmploymentStatus())
                    .department(staff.getDepartment())
                    .jobTitle(staff.getJobTitle())
                    .createdAt(staff.getCreatedAt())
                    .build();


        } catch (Exception ex) {

            if (keycloakUserId != null) {
                keycloakAdminService.deleteUserQuietly(keycloakUserId);
            }

            securityAuditService.record(
                    SecurityAuditEvent.builder()
                            .userId(currentUserProvider.getCurrentUserId())
                            .username(currentUserProvider.getCurrentUsername())
                            .method("POST")
                            .path("/api/v1/admin/staff")
                            .statusCode(500)
                            .action("CREATE_STAFF")
                            .result("FAILED")
                            .errorMessage(ex.getMessage())
                            .durationMs(0L)
                            .occurredAt(Instant.now())
                            .build()
            );

            throw ex;
        }

    }

    private UserRepresentation buildKeycloakUser(AdminStaffReq req) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(true);
        return user;
    }

    private String generateStaffCode() {
        return "STF-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private void validateRequest(AdminStaffReq req) {

        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BusinessException(
                    "Username already exists",
                    ErrorCode.USERNAME_ALREADY_EXISTS,
                    HttpStatus.CONFLICT.value()
            );
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BusinessException(
                    "Email already exists",
                    ErrorCode.EMAIL_ALREADY_EXISTS,
                    HttpStatus.CONFLICT.value()
            );
        }

        if (staffRepository.existsByPhoneNumber(req.getPhoneNumber())) {
            throw new BusinessException(
                    "Phone number already exists",
                    ErrorCode.PHONE_ALREADY_EXISTS,
                    HttpStatus.CONFLICT.value()
            );
        }
    }
}
