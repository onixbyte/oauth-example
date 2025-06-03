package com.onixbyte.oauth.exception;

import org.springframework.http.HttpStatus;

public class PropertyMissingException extends BizException {

    public PropertyMissingException(String propertyName) {
        super(HttpStatus.BAD_REQUEST, "Property [" + propertyName + "] not found.");
    }
}
