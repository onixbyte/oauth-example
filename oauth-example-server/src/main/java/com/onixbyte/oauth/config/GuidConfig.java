package com.onixbyte.oauth.config;

import com.onixbyte.identitygenerator.IdentityGenerator;
import com.onixbyte.identitygenerator.impl.SnowflakeIdentityGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuidConfig {

    @Bean
    public IdentityGenerator<Long> userGuidCreator() {
        return new SnowflakeIdentityGenerator(0x0, 0x0);
    }
}
