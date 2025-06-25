package com.onixbyte.oauth.service;

import com.onixbyte.oauth.data.PublicKeyComponent;
import com.onixbyte.oauth.entity.response.MsalKeyResponse;
import com.onixbyte.oauth.properties.MsalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

/**
 * Services for Microsoft Entra ID authentication.
 *
 * @author siujamo
 */
@Service
public class MsalService {

    private static final Logger log = LoggerFactory.getLogger(MsalService.class);

    private static final String CACHE_PREFIX = "public-key:ms:";

    private final WebClient webClient;
    private final MsalProperties msalProperties;
    private final RedisTemplate<String, PublicKeyComponent> publicKeyCache;

    @Autowired
    public MsalService(
            WebClient webClient,
            MsalProperties msalProperties,
            RedisTemplate<String, PublicKeyComponent> publicKeyCache
    ) {
        this.webClient = webClient;
        this.msalProperties = msalProperties;
        this.publicKeyCache = publicKeyCache;
    }

    /**
     * Get a public key with given key id.
     *
     * @param keyId the key id
     * @return the RSA Public key found by the given key id
     * @throws NoSuchAlgorithmException if no {@code Provider} supports a {@code KeyFactorySpi}
     *                                  implementation for the specified algorithm
     * @throws InvalidKeySpecException  if the given key specification is inappropriate for this key
     *                                  factory to produce a public key
     * @see #getRSAPublicKeyFromModulusExponent(String, String)
     */
    public RSAPublicKey getPublicKey(String keyId) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // get public key from cache
        var msalCache = publicKeyCache.opsForValue().get(CACHE_PREFIX + keyId);
        if (Objects.nonNull(msalCache)) {
            // no keys in the cache, then download keys from Microsoft JWKS cache
            return getRSAPublicKeyFromModulusExponent(msalCache.modulus(), msalCache.exponent());
        }
        return retrievePublicKey(keyId);
    }

    /**
     * Get tenant ID.
     *
     * @return tenant ID
     */
    public String getTenantId() {
        return msalProperties.getTenantId();
    }

    /**
     * Get client ID.
     *
     * @return client ID
     */
    public String getClientId() {
        return msalProperties.getClientId();
    }

    /**
     * Download public key from Microsoft JWKS cache. This operation will save all downloaded public
     * keys to Redis for 24 hours.
     *
     * @param keyId the id of required public key
     * @return the required public key
     * @throws NoSuchAlgorithmException if no {@code Provider} supports a {@code KeyFactorySpi}
     *                                  implementation for the specified algorithm
     * @throws InvalidKeySpecException  if the given key specification is inappropriate for this key
     *                                  factory to produce a public key
     * @see #getRSAPublicKeyFromModulusExponent(String, String)
     */
    private RSAPublicKey retrievePublicKey(String keyId)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        var response = webClient.get()
                .uri("https://login.microsoftonline.com/{tenantId}/discovery/v2.0/keys",
                        msalProperties.getTenantId())
                .retrieve()
                .bodyToMono(MsalKeyResponse.class)
                .block();

        if (response == null || response.keys().isEmpty()) {
            throw new RuntimeException("No keys found in JWKS response");
        }

        PublicKeyComponent matchedKeySpec = null;
        for (var key : response.keys()) {
            var spec = PublicKeyComponent.builder()
                    .withModulus(key.n())
                    .withExponent(key.e())
                    .build();

            // 缓存 KeySpec
            publicKeyCache.opsForValue().set(CACHE_PREFIX + key.kid(), spec);

            // 找到匹配的 keyId
            if (key.kid().equals(keyId)) {
                matchedKeySpec = spec;
            }
        }

        if (matchedKeySpec == null) {
            throw new RuntimeException("Key ID " + keyId + " not found in keys");
        }

        return getRSAPublicKeyFromModulusExponent(matchedKeySpec.modulus(), matchedKeySpec.exponent());
    }

    /**
     * Get the public key with given modulus and public exponent.
     *
     * @param n the modulus
     * @param e the public exponent
     * @return generated public key object from the provided key specification
     * @throws NoSuchAlgorithmException if no {@code Provider} supports a {@code KeyFactorySpi} implementation for the
     *                                  specified algorithm
     * @throws InvalidKeySpecException  if the given key specification is inappropriate for this key factory to produce
     *                                  a public key
     * @see KeyFactory#getInstance(String)
     * @see KeyFactory#generatePublic(java.security.spec.KeySpec)
     */
    private static RSAPublicKey getRSAPublicKeyFromModulusExponent(String n, String e) throws NoSuchAlgorithmException, InvalidKeySpecException {
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
