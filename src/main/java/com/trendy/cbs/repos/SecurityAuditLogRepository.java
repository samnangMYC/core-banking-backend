package com.trendy.cbs.repos;

import com.trendy.cbs.entity.SecurityAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityAuditLogRepository extends JpaRepository<SecurityAuditLog, Long> {
}
