package com.onixbyte.oauth.data.response;

public record MsalKeyResponse(
        String keyId,
        String modulus,
        String exponent,
        String issuer
) {
}
