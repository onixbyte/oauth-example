package com.onixbyte.oauth.entity.response;

import com.onixbyte.oauth.data.MicrosoftPublicKey;

import java.util.List;

public record MsalKeyResponse(
        List<MicrosoftPublicKey> keys
) {
}
