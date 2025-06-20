package com.onixbyte.oauth.data.response;

import java.time.LocalDateTime;

public record TotpRequiredResponse(
        String userId,
        String message,
        LocalDateTime timestamp
) implements BaseResponse {
}
