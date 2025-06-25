package com.onixbyte.oauth.entity.request;

public record NormalAuthenticationRequest(
        String username,
        String password
) {
}
