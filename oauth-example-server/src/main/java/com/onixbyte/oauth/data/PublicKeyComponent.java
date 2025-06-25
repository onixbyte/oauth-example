package com.onixbyte.oauth.data;

public record PublicKeyComponent(
        String modulus,
        String exponent
) {

    public static PublicKeyComponentBuilder builder() {
        return new PublicKeyComponentBuilder();
    }

    public static class PublicKeyComponentBuilder {
        private String modulus;
        private String exponent;

        private PublicKeyComponentBuilder() {
        }

        public PublicKeyComponentBuilder withModulus(String modulus) {
            this.modulus = modulus;
            return this;
        }

        public PublicKeyComponentBuilder withExponent(String exponent) {
            this.exponent = exponent;
            return this;
        }

        public PublicKeyComponent build() {
            return new PublicKeyComponent(modulus, exponent);
        }
    }
}
