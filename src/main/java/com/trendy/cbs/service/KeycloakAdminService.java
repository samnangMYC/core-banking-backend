package com.trendy.cbs.service;


import com.trendy.cbs.enums.SystemRole;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakAdminService {
    String createUser(UserRepresentation user);

    void setPassword(String userId, String password);

    void assignRealmRole(String userId, SystemRole roleName);

    void deleteUserQuietly(String userId);
}
