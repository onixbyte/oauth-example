package com.onixbyte.oauth.data.cache;

public record MsalCache(
        String n,
        String e
) {

    public static MsalCacheBuilder builder() {
        return new MsalCacheBuilder();
    }

    public static class MsalCacheBuilder {
        private String n;

        private String e;

        private MsalCacheBuilder() {
        }

        public MsalCacheBuilder withN(String n) {
            this.n = n;
            return this;
        }

        public MsalCacheBuilder withE(String e) {
            this.e = e;
            return this;
        }

        public MsalCache build() {
            return new MsalCache(n, e);
        }
    }
}
