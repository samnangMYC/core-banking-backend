package com.trendy.cbs.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "security_audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String username;

    private String method;
    private String path;
    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String userAgent;

    private Integer statusCode;

    private String action;
    private String result;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private Long durationMs;
    private Instant occurredAt;
}