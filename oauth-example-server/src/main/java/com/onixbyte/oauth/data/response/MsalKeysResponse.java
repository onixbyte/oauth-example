package com.onixbyte.oauth.data.response;

import java.util.List;

public record MsalKeysResponse(
        List<MsalKeyResponse> keys
) {
}
