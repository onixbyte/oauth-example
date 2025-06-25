package com.onixbyte.oauth.entity.response;

import java.time.LocalDateTime;

public interface BaseResponse {

    public String message();

    public LocalDateTime timestamp();
}
