package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.SecurityAuditEvent;

public interface SecurityAuditService {

     void record(SecurityAuditEvent event);
}
