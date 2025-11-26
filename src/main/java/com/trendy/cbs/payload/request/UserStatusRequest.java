package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusRequest {

    @NotNull(message = "User status is required")
    private UserStatus status;
}
