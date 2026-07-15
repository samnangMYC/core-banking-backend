package com.trendy.cbs.service.keycloak;

import lombok.Getter;

@Getter
public class KeycloakAuthException extends RuntimeException {

    private final String error;
    private final String errorDescription;

    public KeycloakAuthException(String error, String errorDescription) {
        super(errorDescription != null ? errorDescription : error);
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public boolean isOtpRelated() {
        return containsOtpHint(error) || containsOtpHint(errorDescription);
    }

    private boolean containsOtpHint(String value) {
        if (value == null) {
            return false;
        }
        String lower = value.toLowerCase();
        return lower.contains("otp") || lower.contains("totp");
    }
}