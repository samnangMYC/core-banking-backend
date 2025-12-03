package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.IdentityType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentityDocRequest {

    @NotNull(message = "Identity type is required")
    private IdentityType identityType;

    @NotBlank(message = "Document number is required")
    @Size(max = 50, message = "Document number must be at most 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "Document number contains invalid characters") // Prevent injection
    private String number;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDateTime expDate;

    @NotBlank(message = "Document image is required")
    @Size(max = 5_000_000, message = "Document image is too large") // Max ~5MB
   // @Pattern(regexp = "^[A-Za-z0-9+/=\\r\\n]+$", message = "Invalid Base64 format") // Base64 validation
    private String docImage;
}
