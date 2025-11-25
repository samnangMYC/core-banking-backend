package com.trendy.cbs.payload.dto;
import com.trendy.cbs.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserDTO {
    private Long userId;
    private UserProfileDTO profile;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
