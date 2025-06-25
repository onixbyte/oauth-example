package com.onixbyte.oauth.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.onixbyte.identitygenerator.IdentityGenerator;
import com.onixbyte.oauth.properties.TokenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@EnableConfigurationProperties(TokenProperties.class)
public class TokenConfig {

    private final TokenProperties tokenProperties;

    @Autowired
    public TokenConfig(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Bean
    public IdentityGenerator<String> tokenIdentityGenerator() {
        return () -> UUID.randomUUID().toString();
    }

    @Bean
    public Algorithm tokenAlgorithm() {
        return Algorithm.HMAC256(tokenProperties.getSecret());
    }

    @Bean
    public String tokenIssuer() {
        return tokenProperties.getIssuer();
    }
}
