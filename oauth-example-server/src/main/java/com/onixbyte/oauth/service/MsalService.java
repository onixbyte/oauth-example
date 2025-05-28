package com.onixbyte.oauth.service;

import com.onixbyte.oauth.data.cache.MsalCache;
import com.onixbyte.oauth.data.response.MsalKeysResponse;
import com.onixbyte.oauth.properties.MsalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;

@Service
public class MsalService {

    private static final Logger log = LoggerFactory.getLogger(MsalService.class);

    private static final String CACHE_PREFIX = "msal:public-key:";

    private final WebClient webClient;
    private final MsalProperties msalProperties;
    private final RedisTemplate<String, MsalCache> msalRedisTemplate;

    public MsalService(WebClient webClient, MsalProperties msalProperties, RedisTemplate<String, MsalCache> msalRedisTemplate) {
        this.webClient = webClient;
        this.msalProperties = msalProperties;
        this.msalRedisTemplate = msalRedisTemplate;
    }

    public RSAPublicKey getPublicKey(String keyId) throws Exception {
        var msalCache = msalRedisTemplate.opsForValue().get(CACHE_PREFIX + keyId);
        if (Objects.nonNull(msalCache)) {
            return getRSAPublicKeyFromModulusExponent(msalCache.n(), msalCache.e());
        }
        return retrievePublicKey(keyId);
    }

    public String getTenantId() {
        return msalProperties.getTenantId();
    }

    public String getClientId() {
        return msalProperties.getClientId();
    }

    private RSAPublicKey retrievePublicKey(String keyId) throws Exception {
        var response = webClient.get()
                .uri("https://login.microsoftonline.com/{tenantId}/discovery/v2.0/keys", msalProperties.getTenantId())
                .retrieve()
                .bodyToMono(MsalKeysResponse.class)
                .block();

        if (response == null || response.keys() == null) {
            throw new RuntimeException("No keys found in JWKS response");
        }

        // save all public keys to cache
        response.keys().forEach((key) ->
                msalRedisTemplate.opsForValue().set(
                        CACHE_PREFIX + key.kid(),
                        MsalCache.builder()
                                .withN(key.n())
                                .withE(key.e())
                                .build(),
                        Duration.ofHours(24)
                )
        );

        // find specific key with key id
        var maybeKey = response.keys()
                .stream()
                .filter((key) -> key.kid().equals(keyId))
                .findFirst();

        if (maybeKey.isEmpty()) {
            throw new RuntimeException("Key ID " + keyId + " not found in JWKS");
        }

        var key = maybeKey.get();
        return getRSAPublicKeyFromModulusExponent(key.n(), key.e());
    }

    private static RSAPublicKey getRSAPublicKeyFromModulusExponent(String n, String e) throws Exception {
        var urlDecoder = Base64.getUrlDecoder();

        var modulus = new BigInteger(1, urlDecoder.decode(n));
        var exponent = new BigInteger(1, urlDecoder.decode(e));

        var keySpec = new RSAPublicKeySpec(modulus, exponent);
        var kf = KeyFactory.getInstance("RSA");
        if (kf.generatePublic(keySpec) instanceof RSAPublicKey rsaPublicKey) {
            return rsaPublicKey;
        } else {
            throw new RuntimeException("Cannot generate RSA public key with given modulus and exponent.");
        }
    }
}
