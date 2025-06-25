package com.onixbyte.oauth.authentication.token;

import com.onixbyte.oauth.model.table.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class TotpToken implements Authentication {

    private Long userId;

    private String totp;

    private User details;

    private boolean authenticated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getCredentials() {
        return totp;
    }

    @Override
    public User getDetails() {
        return details;
    }

    @Override
    public Long getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return details.getUsername();
    }

    public void setPrinciple(Long principle) {
        this.userId = principle;
    }

    public void setCredentials(String totp) {
        this.totp = totp;
    }

    public void setDetails(User details) {
        this.details = details;
    }

    public TotpToken() {
    }

    public TotpToken(Long userId, String totp, User details, boolean authenticated) {
        this.userId = userId;
        this.totp = totp;
        this.details = details;
        this.authenticated = authenticated;
    }

    public static TotpTokenBuilder builder() {
        return new TotpTokenBuilder();
    }

    public static class TotpTokenBuilder {
        private Long userId;
        private String totp;
        private User details;
        private boolean authenticated;

        private TotpTokenBuilder() {
        }

        public TotpTokenBuilder withPrinciple(Long principle) {
            this.userId = principle;
            return this;
        }

        public TotpTokenBuilder withCredentials(String credentials) {
            this.totp = credentials;
            return this;
        }

        public TotpTokenBuilder withDetails(User details) {
            this.details = details;
            return this;
        }

        public TotpTokenBuilder withAuthenticated(boolean authenticated) {
            this.authenticated = authenticated;
            return this;
        }

        public TotpToken build() {
            return new TotpToken(userId, totp, details, authenticated);
        }
    }

    public static TotpToken unauthenticated(Long principle, String credentials) {
        return TotpToken.builder()
                .withPrinciple(principle)
                .withCredentials(credentials)
                .build();
    }

    public static TotpToken authenticated(User user) {
        return TotpToken.builder()
                .withPrinciple(user.getId())
                .withCredentials("")
                .withAuthenticated(true)
                .build();
    }
}
