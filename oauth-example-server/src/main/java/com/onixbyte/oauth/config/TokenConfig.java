package com.onixbyte.oauth.config;

import com.onixbyte.identitygenerator.IdentityGenerator;
import com.onixbyte.oauth.properties.TokenProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@EnableConfigurationProperties(TokenProperties.class)
public class TokenConfig {

    @Bean
    public IdentityGenerator<String> tokenIdentityGenerator() {
        return () -> UUID.randomUUID().toString();
    }
}
