package com.onixbyte.oauth.data.response;

public record MsalKeyResponse(
        String kid,
        String n,
        String e,
        String issuer
) {
}
