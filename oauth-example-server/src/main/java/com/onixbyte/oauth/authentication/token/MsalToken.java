package com.onixbyte.oauth.authentication.token;

import com.onixbyte.oauth.data.persistent.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class MsalToken implements Authentication, CredentialsContainer {

    private String msalOpenId;

    public MsalToken(String msalOpenId) {
        this.msalOpenId = msalOpenId;
    }

    public static MsalToken unauthenticated(String msalOpenId) {
        return new MsalToken(msalOpenId);
    }

    public String getMsalOpenId() {
        return msalOpenId;
    }

    public void setMsalOpenId(String msalOpenId) {
        this.msalOpenId = msalOpenId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getCredentials() {
        return "";
    }

    @Override
    public User getDetails() {

        return null;
    }

    @Override
    public String getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void eraseCredentials() {

    }
}
