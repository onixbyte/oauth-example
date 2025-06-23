package com.onixbyte.oauth.authentication.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.onixbyte.oauth.authentication.token.MsalToken;
import com.onixbyte.oauth.data.persistent.User;
import com.onixbyte.oauth.exception.BizException;
import com.onixbyte.oauth.service.MsalService;
import com.onixbyte.oauth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * {@code MsalAuthenticationProvider} provides authentication by Microsoft Entra ID.
 *
 * @author zihluwang
 * @author siujamo
 */
@Component
public class MsalAuthenticationProvider implements AuthenticationProvider {

    private final MsalService msalService;
    private final UserService userService;

    public MsalAuthenticationProvider(
            UserService userService,
            MsalService msalService
    ) {
        this.userService = userService;
        this.msalService = msalService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof MsalToken token) {
            // decode JWT to retrieve kid in header
            var decodedToken = JWT.decode(token.getPrincipal());
            var keyId = decodedToken.getKeyId();
            if (keyId == null) {
                throw new BizException(HttpStatus.BAD_REQUEST, "Token header missing key id.");
            }

            try {
                // get RSAPublicKey from Microsoft JWKS cache according to kid
                var publicKey = msalService.getPublicKey(keyId);

                // create algorithm instance
                var algorithm = Algorithm.RSA256(publicKey, null);

                // create verifier and verify `issuer` and `aud`
                var verifier = JWT.require(algorithm)
                        // format of the issuer is `https://login.microsoftonline.com/{tenantId}/v2.0`
                        .withIssuer("https://login.microsoftonline.com/" + msalService.getTenantId() + "/v2.0")
                        // client id is used for audience
                        .withAudience(msalService.getClientId())
                        .build();

                // verify token
                verifier.verify(token.getPrincipal());

                // get open id from token
                var msalOpenId = decodedToken.getClaim("oid").asString();

                userService.getUserByMsalOpenId(msalOpenId)
                        .ifPresentOrElse((user) -> { // user is registered, login directly
                            token.setDetails(user);
                            token.setAuthenticated(true);
                        }, () -> { // user has not registered yet, automatically register a new account
                            var username = decodedToken.getClaim("name").asString();
                            var email = decodedToken.getClaim("preferred_username").asString();

                            var user = userService.register(User.builder()
                                    .withUsername(username)
                                    .withEmail(email)
                                    .withMsalOpenId(msalOpenId)
                                    .withTotpSecret(null)
                                    .build());

                            token.setDetails(user);
                            token.setAuthenticated(true);
                        });
                return token;
            } catch (NoSuchAlgorithmException e) {
                throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, "Server does not support this type of encryption algorithm.");
            } catch (InvalidKeySpecException e) {
                throw new BizException(HttpStatus.BAD_REQUEST, "Cannot generate public key from provided key spec.");
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MsalToken.class.isAssignableFrom(authentication);
    }
}
