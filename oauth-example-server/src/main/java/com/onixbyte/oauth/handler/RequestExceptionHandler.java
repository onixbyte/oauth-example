package com.onixbyte.oauth.handler;

import com.onixbyte.oauth.data.response.BizExceptionResponse;
import com.onixbyte.oauth.exception.BizException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<BizExceptionResponse> handleBizException(BizException bizException) {
        return bizException.composeResponse();
    }
}
