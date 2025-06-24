package com.onixbyte.oauth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.onixbyte.identitygenerator.IdentityGenerator;
import com.onixbyte.oauth.data.persistent.User;
import com.onixbyte.oauth.properties.TokenProperties;
import com.onixbyte.oauth.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TokenService {

    private final Algorithm algorithm;
    private final IdentityGenerator<String> tokenIdentityGenerator;
    private final String issuer;

    @Autowired
    public TokenService(
            TokenProperties tokenProperties,
            IdentityGenerator<String> tokenIdentityGenerator
    ) {
        algorithm = Algorithm.HMAC256(tokenProperties.getSecret());
        this.tokenIdentityGenerator = tokenIdentityGenerator;
        this.issuer = tokenProperties.getIssuer();
    }

    public String createToken(Duration expiresAfter, User user) {
        var currentTime = LocalDateTime.now();
        return JWT.create()
                .withExpiresAt(DateTimeUtil.toInstant(currentTime.plus(expiresAfter)))
                .withNotBefore(DateTimeUtil.toInstant(currentTime))
                .withIssuedAt(DateTimeUtil.toInstant(currentTime))
                .withJWTId(tokenIdentityGenerator.nextId())
                .withIssuer(issuer)
                .withSubject("OnixByte User")
                .withAudience(String.valueOf(user.getId()))
                .sign(algorithm);
    }
}
