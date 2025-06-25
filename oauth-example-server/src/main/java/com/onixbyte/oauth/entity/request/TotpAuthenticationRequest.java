package com.onixbyte.oauth.entity.request;

public record TotpAuthenticationRequest(
        Long userId,
        String totp
) {
}
