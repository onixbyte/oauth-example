package com.onixbyte.oauth.authentication.provider;

import com.onixbyte.oauth.authentication.token.MsalToken;
import com.onixbyte.oauth.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class MsalAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    public MsalAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MsalToken.class.isAssignableFrom(authentication);
    }
}
