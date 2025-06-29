package com.onixbyte.oauth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.identitygenerator.IdentityGenerator;
import com.onixbyte.oauth.model.User;
import com.onixbyte.oauth.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TokenService {

    private final IdentityGenerator<String> tokenIdentityGenerator;
    private final Algorithm algorithm;
    private final String issuer;

    @Autowired
    public TokenService(
            IdentityGenerator<String> tokenIdentityGenerator,
            Algorithm algorithm,
            String issuer
    ) {
        this.tokenIdentityGenerator = tokenIdentityGenerator;
        this.algorithm = algorithm;
        this.issuer = issuer;
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

    public DecodedJWT verify(String token) {
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
    }
}
