package com.onixbyte.oauth.exception;

import com.onixbyte.oauth.data.response.BaseResponse;
import com.onixbyte.oauth.data.response.BizExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseBizException extends RuntimeException {

    protected final HttpStatus status;

    public BaseBizException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public abstract ResponseEntity<? extends BaseResponse> composeResponse();
}
