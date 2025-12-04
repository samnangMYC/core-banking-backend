package com.trendy.cbs.payload.request;

import com.trendy.cbs.enums.DocStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityDocStatusRequest {

    @Enumerated(EnumType.STRING)
    private DocStatus docStatus;
}
