package com.onixbyte.oauth.handler;

import com.onixbyte.oauth.entity.response.BizExceptionResponse;
import com.onixbyte.oauth.entity.response.TotpRequiredResponse;
import com.onixbyte.oauth.exception.BizException;
import com.onixbyte.oauth.exception.TotpRequiredException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<BizExceptionResponse> handleBizException(BizException bizException) {
        return bizException.composeResponse();
    }

    @ExceptionHandler(TotpRequiredException.class)
    public ResponseEntity<TotpRequiredResponse> handleTotpRequiredException(TotpRequiredException totpRequiredException) {
        return totpRequiredException.composeResponse();
    }
}
