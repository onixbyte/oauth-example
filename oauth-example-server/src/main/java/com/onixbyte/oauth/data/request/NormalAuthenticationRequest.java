package com.onixbyte.oauth.data.request;

public record NormalAuthenticationRequest(
        String username,
        String password
) {
}
