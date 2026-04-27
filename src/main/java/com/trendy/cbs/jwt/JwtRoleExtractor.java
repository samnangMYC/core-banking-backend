package com.trendy.cbs.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class JwtRoleExtractor {

    private final String clientId;

    public JwtRoleExtractor(@Value("${keycloak.public-client-id}") String clientId) {
        this.clientId = clientId;
    }

    public Set<String> extractRoles(String token) {

        DecodedJWT jwt = JWT.decode(token);
        Set<String> roles = new HashSet<>();

        extractRealmRoles(jwt, roles);
        extractClientRoles(jwt, roles);

        return roles;
    }

    private void extractRealmRoles(DecodedJWT jwt, Set<String> roles) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access").asMap();

        if (realmAccess != null && realmAccess.get("roles") instanceof List<?> list) {
            list.forEach(r -> roles.add("ROLE_" + r));
        }
    }

    private void extractClientRoles(DecodedJWT jwt, Set<String> roles) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access").asMap();

        if (resourceAccess == null) return;

        Object client = resourceAccess.get(clientId);

        if (client instanceof Map<?, ?> clientMap) {
            Object clientRoles = clientMap.get("roles");

            if (clientRoles instanceof List<?> list) {
                list.forEach(r -> roles.add("ROLE_" + r));
            }
        }
    }
}