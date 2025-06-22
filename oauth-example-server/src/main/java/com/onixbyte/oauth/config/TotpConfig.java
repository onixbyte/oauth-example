package com.onixbyte.oauth.config;

import com.onixbyte.oauth.properties.TotpProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TotpProperties.class)
public class TotpConfig {
}
