package com.onixbyte.oauth.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.oauth.authentication.token.MsalToken;
import com.onixbyte.oauth.data.request.MsalAuthoriseRequest;
import com.onixbyte.oauth.data.response.UserResponse;
import com.onixbyte.oauth.exception.BizException;
import com.onixbyte.simplejwt.TokenResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authorisation")
public class AuthorisationController {

    private static final Logger log = LoggerFactory.getLogger(AuthorisationController.class);

    private final AuthenticationManager authenticationManager;
    private final TokenResolver<DecodedJWT> tokenResolver;

    public AuthorisationController(AuthenticationManager authenticationManager, TokenResolver<DecodedJWT> tokenResolver) {
        this.authenticationManager = authenticationManager;
        this.tokenResolver = tokenResolver;
    }

    /**
     * Authorisation for Microsoft Entra ID.
     *
     * @param msalAuthoriseRequest authorisation request for Microsoft Entra ID
     */
    @PostMapping("/msal")
    public ResponseEntity<UserResponse> msalAuthorisation(@RequestBody MsalAuthoriseRequest msalAuthoriseRequest) {
        // get id token from frontend
        var idToken = msalAuthoriseRequest.idToken();
        var authenticatedToken = authenticationManager.authenticate(MsalToken.unauthenticated(idToken));
        if (authenticatedToken instanceof MsalToken msalToken) {
            log.info("User logged in with Microsoft Entra ID: {}", msalToken.getDetails().getMsalOpenId());
            var user = msalToken.getDetails();
            var authorisationToken = tokenResolver.createToken(
                    Duration.ofDays(1),
                    "oauth-example",
                    msalToken.getName(),
                    Map.of("uid", String.valueOf(user.getId()))
            );
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Authorization", authorisationToken)
                    .body(user.asResponse());
        }

        throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR, "Server crushed, please try again later.");
    }
}
