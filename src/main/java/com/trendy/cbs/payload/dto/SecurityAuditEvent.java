package com.trendy.cbs.payload.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record SecurityAuditEvent(
        String userId,
        String username,
        String method,
        String path,
        String ipAddress,
        String userAgent,
        int statusCode,
        String action,
        String result,
        String errorMessage,
        long durationMs,
        Instant occurredAt
) {

}
