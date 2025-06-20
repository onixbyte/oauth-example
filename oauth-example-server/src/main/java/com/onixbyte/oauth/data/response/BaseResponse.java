package com.onixbyte.oauth.data.response;

import java.time.LocalDateTime;

public interface BaseResponse {

    public String message();

    public LocalDateTime timestamp();
}
