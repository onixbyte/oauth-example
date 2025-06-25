package com.onixbyte.oauth.entity.response;

import java.time.LocalDateTime;

public record TotpRequiredResponse(
        String userId,
        String message,
        LocalDateTime timestamp
) implements BaseResponse {
}
