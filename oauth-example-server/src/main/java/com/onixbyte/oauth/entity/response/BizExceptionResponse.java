package com.onixbyte.oauth.entity.response;

import java.time.LocalDateTime;
import java.util.Optional;

public record BizExceptionResponse(
        LocalDateTime timestamp,
        String message
) implements BaseResponse {

    public static BizExceptionResponseBuilder builder() {
        return new BizExceptionResponseBuilder();
    }

    public static class BizExceptionResponseBuilder {
        private LocalDateTime timestamp;
        private String message;

        private BizExceptionResponseBuilder() {
        }

        public BizExceptionResponseBuilder withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public BizExceptionResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public BizExceptionResponse build() {
            var _timestamp = Optional.ofNullable(timestamp)
                    .orElseGet(LocalDateTime::now);
            return new BizExceptionResponse(_timestamp, message);
        }
    }
}
