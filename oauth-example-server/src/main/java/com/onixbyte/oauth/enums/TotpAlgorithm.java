package com.onixbyte.oauth.enums;

public enum TotpAlgorithm {

    HMAC_SHA_1("HmacSHA1"),
    ;

    private final String algorithm;

    TotpAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
