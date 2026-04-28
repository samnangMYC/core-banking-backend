package com.trendy.cbs.service.factory;

import com.trendy.cbs.payload.dto.SecurityAuditEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class SecurityAuditEventFactory {

    public SecurityAuditEvent from(
            HttpServletRequest request,
            HttpServletResponse response,
            long startedAt,
            Exception exception
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = extractJwt(auth);

        return SecurityAuditEvent.builder()
                .userId(jwt != null ? jwt.getSubject() : null)
                .username(resolveUsername(auth, jwt))
                .method(request.getMethod())
                .path(request.getRequestURI())
                .ipAddress(resolveIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .statusCode(response.getStatus())
                .action(resolveAction(request))
                .result(response.getStatus() >= 400 ? "FAILED" : "SUCCESS")
                .errorMessage(exception != null ? exception.getMessage() : null)
                .durationMs(System.currentTimeMillis() - startedAt)
                .occurredAt(Instant.now())
                .build();
    }



    private Jwt extractJwt(Authentication auth) {
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        return null;
    }

    private String resolveUsername(Authentication auth, Jwt jwt) {

        if (jwt != null) {
            log.info("Resolving username: {}", jwt.getClaimAsString("preferred_username"));
            return jwt.getClaimAsString("preferred_username");
        }

        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }

        return "ANONYMOUS";
    }

    private String resolveIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    private String resolveAction(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (path.contains("/auth/sign-in")) return "LOGIN";
        if (path.contains("/auth/sign-out")) return "LOGOUT";

        return switch (method) {
            case "POST" -> "CREATE";
            case "PUT", "PATCH" -> "UPDATE";
            case "DELETE" -> "DELETE";
            case "GET" -> "VIEW";
            default -> "UNKNOWN";
        };
    }
}
