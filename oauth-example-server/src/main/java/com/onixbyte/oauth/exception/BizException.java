package com.onixbyte.oauth.exception;

import com.onixbyte.oauth.entity.response.BizExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BizException extends BaseBizException {

    public BizException(HttpStatus status, String message) {
        super(status, message);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ResponseEntity<BizExceptionResponse> composeResponse() {
        return ResponseEntity.status(getStatus())
                .body(BizExceptionResponse.builder()
                        .withMessage(getMessage())
                        .build());
    }
}
