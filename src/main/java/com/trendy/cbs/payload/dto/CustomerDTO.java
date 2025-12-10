package com.trendy.cbs.payload.dto;
import com.trendy.cbs.enums.CustomerStatus;
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
 * @see CustomerProfileDTO
 * @see CustomerStatus
 */
@Data
@Builder
public class CustomerDTO {
    private Long cusId;
    private CustomerProfileDTO profile;
    private CustomerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
