package com.onixbyte.oauth.service;

import com.onixbyte.oauth.data.cache.MsalCache;
import com.onixbyte.oauth.data.response.MsalKeyResponse;
import com.onixbyte.oauth.data.response.MsalKeysResponse;
import com.onixbyte.oauth.properties.MsalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Services for Microsoft Entra ID authorisations.
 *
 * @author siujamo
 */
@Service
public class MsalService {

    private static final Logger log = LoggerFactory.getLogger(MsalService.class);

    private static final String CACHE_PREFIX = "msal:public-key:";

    private final WebClient webClient;
    private final MsalProperties msalProperties;
    private final RedisTemplate<String, MsalCache> msalRedisTemplate;

    @Autowired
    public MsalService(
            WebClient webClient,
            MsalProperties msalProperties,
            RedisTemplate<String, MsalCache> msalRedisTemplate
    ) {
        this.webClient = webClient;
        this.msalProperties = msalProperties;
        this.msalRedisTemplate = msalRedisTemplate;
    }

    /**
     * Get a public key with given key id.
     *
     * @param keyId the key id
     * @return the RSA Public key found by the given key id
     * @throws NoSuchAlgorithmException if no {@code Provider} supports a {@code KeyFactorySpi} implementation for the
     *                                  specified algorithm
     * @throws InvalidKeySpecException  if the given key specification is inappropriate for this key factory to produce
     *                                  a public key
     * @see #getRSAPublicKeyFromModulusExponent(String, String)
     */
    public RSAPublicKey getPublicKey(String keyId) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // get public key from cache
        var msalCache = msalRedisTemplate.opsForValue().get(CACHE_PREFIX + keyId);
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
     * Download public key from Microsoft JWKS cache. This operation will save all downloaded public keys to Redis for
     * 24 hours.
     *
     * @param keyId the id of required public key
     * @return the required public key
     * @throws NoSuchAlgorithmException if no {@code Provider} supports a {@code KeyFactorySpi} implementation for the
     *                                  specified algorithm
     * @throws InvalidKeySpecException  if the given key specification is inappropriate for this key factory to produce
     *                                  a public key
     * @see #getRSAPublicKeyFromModulusExponent(String, String)
     */
    private RSAPublicKey retrievePublicKey(String keyId) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var response = webClient.get()
                .uri("https://login.microsoftonline.com/{tenantId}/discovery/v2.0/keys", msalProperties.getTenantId())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, List<HashMap<String, Object>>>>() {
                })
                .block();

        if (response == null || response.isEmpty()) {
            throw new RuntimeException("No keys found in JWKS response");
        }

        // save all public keys to cache
        var keys = response.get("keys")
                .stream()
                .map((keyMap) -> {
                    String kid = (String) keyMap.get("kid");
                    String n = (String) keyMap.get("n");
                    String e = (String) keyMap.get("e");
                    String issuer = (String) keyMap.get("issuer");
                    var key = new MsalKeyResponse(kid, n, e, issuer);
                    msalRedisTemplate.opsForValue().set(CACHE_PREFIX + key.keyId(), MsalCache.builder()
                            .withModulus(key.modulus())
                            .withExponent(key.exponent())
                            .build());
                    return key;
                })
                .toList();

        // find specific key with key id
        var maybeKey = keys
                .stream()
                .filter((key) -> key.keyId().equals(keyId))
                .findFirst();

        if (maybeKey.isEmpty()) {
            throw new RuntimeException("Key ID " + keyId + " not found in JWKS");
        }

        var key = maybeKey.get();
        return getRSAPublicKeyFromModulusExponent(key.modulus(), key.exponent());
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
     * @see KeyFactory#generatePublic(KeySpec)
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
