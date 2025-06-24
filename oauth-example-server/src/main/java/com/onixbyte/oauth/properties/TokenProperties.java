package com.onixbyte.oauth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.token")
public class TokenProperties {

    private String issuer;

    private String secret;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public TokenProperties() {
    }

    public TokenProperties(String issuer, String secret) {
        this.issuer = issuer;
        this.secret = secret;
    }
}
