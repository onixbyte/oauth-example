package com.onixbyte.oauth.config;

import com.onixbyte.guid.GuidCreator;
import com.onixbyte.guid.impl.SnowflakeGuidCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuidConfig {

    @Bean
    public GuidCreator<Long> userGuidCreator() {
        return new SnowflakeGuidCreator(0x0, 0x0);
    }
}
