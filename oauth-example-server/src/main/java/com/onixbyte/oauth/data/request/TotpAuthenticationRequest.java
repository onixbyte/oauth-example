package com.onixbyte.oauth.data.request;

public record TotpAuthenticationRequest(
        Long userId,
        String totp
) {
}
