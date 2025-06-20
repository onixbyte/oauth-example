package com.onixbyte.oauth.exception;

import com.onixbyte.oauth.data.response.BizExceptionResponse;
import com.onixbyte.oauth.data.response.TotpRequiredResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class TotpRequiredException extends BaseBizException {

    private final Long userId;

    public TotpRequiredException(Long userId, String message) {
        super(HttpStatus.ACCEPTED, message);
        this.userId = userId;
    }

    @Override
    public ResponseEntity<TotpRequiredResponse> composeResponse() {
        return ResponseEntity.status(status)
                .body(new TotpRequiredResponse(
                        String.valueOf(userId),
                        getMessage(),
                        LocalDateTime.now())
                );
    }
}
