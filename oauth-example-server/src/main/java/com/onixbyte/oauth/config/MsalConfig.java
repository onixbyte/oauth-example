package com.onixbyte.oauth.config;

import com.onixbyte.oauth.properties.MsalProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MsalProperties.class)
public class MsalConfig {
}
