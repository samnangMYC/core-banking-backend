package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.SecurityAuditLog;
import com.trendy.cbs.mapper.SecurityAuditMapper;
import com.trendy.cbs.payload.dto.SecurityAuditEvent;
import com.trendy.cbs.repos.SecurityAuditLogRepository;
import com.trendy.cbs.service.SecurityAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityAuditServiceImpl implements SecurityAuditService {

    private final SecurityAuditLogRepository repository;
    private final SecurityAuditMapper mapper;

    @Override
    @Async
    public void record(SecurityAuditEvent event) {
        try {
            SecurityAuditLog entity = mapper.toEntity(event);
            repository.save(entity);
        } catch (Exception ex) {
            log.error("Failed to record security audit log", ex);
        }
    }
}
