package com.onixbyte.oauth.data;

public record MicrosoftPublicKey(
        String kid,
        String n,
        String e
) {
}
