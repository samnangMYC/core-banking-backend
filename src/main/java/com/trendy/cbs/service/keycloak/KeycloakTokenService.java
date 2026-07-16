package com.trendy.cbs.service.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Password grant against the Keycloak token endpoint directly, so an
     * otp/totp form param can be attached. KeycloakBuilder's tokenManager
     * (used by {@link #login(String, String)}) has no way to pass it.
     */
    public AccessTokenResponse loginWithOtp(String username, String password, String otp) {
        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", OAuth2Constants.PASSWORD);
        form.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }
        form.add("username", username);
        form.add("password", password);
        if (otp != null && !otp.isBlank()) {
            form.add("totp", otp);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            return restTemplate
                    .postForEntity(tokenUrl, new HttpEntity<>(form, headers), AccessTokenResponse.class)
                    .getBody();
        } catch (HttpClientErrorException ex) {
            throw toAuthException(ex);
        }
    }

    private KeycloakAuthException toAuthException(HttpClientErrorException ex) {
        try {
            Map<String, String> body = objectMapper.readValue(ex.getResponseBodyAsString(), Map.class);
            return new KeycloakAuthException(body.get("error"), body.get("error_description"));
        } catch (Exception parseEx) {
            return new KeycloakAuthException("invalid_grant", ex.getStatusText());
        }
    }

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