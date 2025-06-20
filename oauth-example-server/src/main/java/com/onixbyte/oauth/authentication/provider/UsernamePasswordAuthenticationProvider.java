package com.onixbyte.oauth.authentication.provider;

import com.onixbyte.oauth.authentication.token.UsernamePasswordToken;
import com.onixbyte.oauth.exception.BizException;
import com.onixbyte.oauth.exception.TotpRequiredException;
import com.onixbyte.oauth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UsernamePasswordAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var token = (UsernamePasswordToken) authentication;
        var user = userService.getUserByUsername(token.getPrincipal());

        if (Objects.isNull(user)) {
            throw new BizException(HttpStatus.BAD_REQUEST, "No such user.");
        }

        if (!passwordEncoder.matches(token.getCredentials(), user.getPassword())) {
            throw new BizException(HttpStatus.BAD_REQUEST, "Password incorrect.");
        }

        if (Boolean.TRUE.equals(user.getTotpEnabled())) {
            throw new TotpRequiredException(user.getId(), "Please complete TOTP authentication.");
        }

        token.setDetails(user);
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordToken.class.isAssignableFrom(authentication);
    }
}
