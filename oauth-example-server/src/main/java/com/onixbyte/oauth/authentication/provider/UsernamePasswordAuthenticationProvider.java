package com.onixbyte.oauth.authentication.provider;

import com.onixbyte.oauth.authentication.token.UsernamePasswordToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordToken.class.isAssignableFrom(authentication);
    }
}
