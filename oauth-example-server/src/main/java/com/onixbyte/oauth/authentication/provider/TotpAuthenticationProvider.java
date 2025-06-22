package com.onixbyte.oauth.authentication.provider;

import com.onixbyte.oauth.authentication.token.TotpToken;
import com.onixbyte.oauth.exception.BizException;
import com.onixbyte.oauth.service.OtpService;
import com.onixbyte.oauth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.time.Duration;

@Component
public class TotpAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final OtpService otpService;

    public TotpAuthenticationProvider(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof TotpToken token) {
            var user = userService.getUserById(token.getPrincipal());
            if (Boolean.FALSE.equals(user.getTotpEnabled())) {
                throw new BizException(HttpStatus.CONFLICT, "You haven't enable TOTP yet.");
            }

            var totp = otpService.generateTotp(user.getTotpSecret());
            if (!totp.equals(token.getCredentials())) {
                throw new BizException(HttpStatus.UNAUTHORIZED, "TOTP incorrect.");
            }

            token.setAuthenticated(true);
            token.setDetails(user);
            return token;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TotpToken.class.isAssignableFrom(authentication);
    }
}
