package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.SecurityAuditLog;
import com.trendy.cbs.payload.dto.SecurityAuditEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SecurityAuditMapper {

    @Mapping(target = "id", ignore = true)
    SecurityAuditLog toEntity(SecurityAuditEvent event);
}
