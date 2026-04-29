package com.trendy.cbs.service.keycloak;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KeycloakTokenService {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.public-client-id}")
    private String clientId;

    @Value("${keycloak.admin-client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public AccessTokenResponse login(String username, String password) {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .grantType(OAuth2Constants.PASSWORD)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .build()
                .tokenManager()
                .getAccessToken();
    }
    public void logout(String refreshToken) {
        String logoutUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("refresh_token", refreshToken);

        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(form, headers);

        restTemplate.postForEntity(logoutUrl, request, Void.class);
    }


}