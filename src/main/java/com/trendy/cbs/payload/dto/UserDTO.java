package com.trendy.cbs.payload.dto;
import com.trendy.cbs.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for representing a User in API responses.
 * Includes essential user details, profile, status, and timestamps.
 *
 * <p>This class uses Lombok annotations to generate getters, setters, toString,
 * equals, hashCode, and a builder pattern for immutable object creation.</p>
 *
 * @see UserProfileDTO
 * @see UserStatus
 */
@Data
@Builder
public class UserDTO {
    private Long userId;
    private UserProfileDTO profile;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
