package com.onixbyte.oauth.data.cache;

public record MsalCache(
        String modulus,
        String exponent
) {

    public static MsalCacheBuilder builder() {
        return new MsalCacheBuilder();
    }

    public static class MsalCacheBuilder {
        private String modulus;

        private String exponent;

        private MsalCacheBuilder() {
        }

        public MsalCacheBuilder withModulus(String modulus) {
            this.modulus = modulus;
            return this;
        }

        public MsalCacheBuilder withExponent(String exponent) {
            this.exponent = exponent;
            return this;
        }

        public MsalCache build() {
            return new MsalCache(modulus, exponent);
        }
    }
}
