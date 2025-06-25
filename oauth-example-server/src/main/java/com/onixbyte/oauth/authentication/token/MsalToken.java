package com.onixbyte.oauth.authentication.token;

import com.onixbyte.oauth.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MsalToken implements Authentication {

    private String idToken;

    private User details;

    private boolean authenticated;

    public MsalToken(String idToken, User details) {
        this.idToken = idToken;
        this.details = details;
        authenticated = Objects.nonNull(details);
    }

    public static MsalToken unauthenticated(String idToken) {
        return new MsalToken(idToken, null);
    }

    public static MsalToken authenticated(User details) {
        return new MsalToken("", details);
    }

    public void setDetails(User details) {
        this.details = details;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Get user's credentials. Credentials are empty for users logged with <b>Microsoft Entra ID</b>.
     *
     * @return credentials of the current user
     */
    @Override
    public String getCredentials() {
        return "";
    }

    @Override
    public User getDetails() {
        return details;
    }

    @Override
    public String getPrincipal() {
        return idToken;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return details.getUsername();
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
