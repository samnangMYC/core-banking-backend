package com.trendy.cbs.audit;

import com.trendy.cbs.payload.dto.SecurityAuditEvent;
import com.trendy.cbs.service.SecurityAuditService;
import com.trendy.cbs.service.factory.SecurityAuditEventFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityAuditFilter extends OncePerRequestFilter {

    private final SecurityAuditEventFactory eventFactory;
    private final SecurityAuditService auditService;

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/v1/admin/auth",
            "/api/v1/customer/auth",
            "/actuator",
            "/swagger",
            "/v3/api-docs"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startedAt = System.currentTimeMillis();
        Exception exception = null;

        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            exception = ex;
            throw ex;
        } finally {
            SecurityAuditEvent event = eventFactory.from(
                    request,
                    response,
                    startedAt,
                    exception
            );

            auditService.record(event);
        }

    }
}
