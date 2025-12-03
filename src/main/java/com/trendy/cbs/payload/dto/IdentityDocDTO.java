package com.trendy.cbs.payload.dto;

import com.trendy.cbs.enums.DocStatus;
import com.trendy.cbs.enums.IdentityType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class IdentityDocDTO {

    private Long docId;

    @Enumerated(EnumType.STRING)
    private IdentityType identityType;

    private String number;

    private LocalDateTime expDate;

    private LocalDateTime verifiedDate;

    private String docImage;

    @Enumerated(EnumType.STRING)
    private DocStatus docStatus;

}
