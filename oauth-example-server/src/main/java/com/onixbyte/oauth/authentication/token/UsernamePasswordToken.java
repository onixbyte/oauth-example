package com.onixbyte.oauth.authentication.token;

import com.onixbyte.oauth.data.persistent.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UsernamePasswordToken implements Authentication, CredentialsContainer {

    private String username;

    private String password;

    private User user;

    private boolean authenticated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public void setCredentials(String credentials) {
        this.password = credentials;
    }

    @Override
    public String getCredentials() {
        return password;
    }

    public void setDetails(User details) {
        this.user = details;
    }

    @Override
    public User getDetails() {
        return user;
    }

    public void setPrinciple(String principle) {
        this.username = principle;
    }

    @Override
    public String getPrincipal() {
        return username;
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
        return username;
    }

    @Override
    public void eraseCredentials() {
        this.password = "";
        this.user.setPassword("");
    }
}
