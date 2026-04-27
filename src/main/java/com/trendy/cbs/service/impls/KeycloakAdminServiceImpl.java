package com.trendy.cbs.service.impls;

import com.trendy.cbs.enums.ErrorCode;
import com.trendy.cbs.enums.SystemRole;
import com.trendy.cbs.exception.BusinessException;
import com.trendy.cbs.service.KeycloakAdminService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakAdminServiceImpl implements KeycloakAdminService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public String createUser(UserRepresentation user) {

        try (Response response = keycloak.realm(realm).users().create(user)) {

            int status = response.getStatus();

            if (status != Response.Status.CREATED.getStatusCode()) {
                String body = safeReadBody(response);

                log.info("Keycloak operation realm={}", realm);

                log.error("Keycloak create user failed status={} body={}", status, body);

                throw new BusinessException(
                        "Failed to create user in Keycloak",
                        ErrorCode.KEYCLOAK_USER_CREATE_FAILED,
                        403
                );
            }

            return CreatedResponseUtil.getCreatedId(response);
        }
    }

    @Override
    public void setPassword(String userId, String password) {

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        keycloak.realm(realm)
                .users()
                .get(userId)
                .resetPassword(credential);
    }

    @Override
    public void assignRealmRole(String userId, SystemRole roleName) {

        RoleRepresentation role = keycloak.realm(realm)
                .roles()
                .get(String.valueOf(roleName))
                .toRepresentation();

        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }

    @Override
    public void deleteUserQuietly(String userId) {
        try {
            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .remove();
        } catch (Exception ex) {
            log.warn("Failed to delete Keycloak user during rollback. userId={}", userId, ex);
        }
    }

    private String safeReadBody(Response response) {
        try {
            return response.hasEntity()
                    ? response.readEntity(String.class)
                    : "";
        } catch (Exception ex) {
            log.warn("Could not read Keycloak error body", ex);
            return "";
        }
    }
}
